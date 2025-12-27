package org.example.demomanagementsystemcproject.controller;

import org.example.demomanagementsystemcproject.dto.MemberProfileDTO;
import org.example.demomanagementsystemcproject.entity.Admin;
import org.example.demomanagementsystemcproject.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    public ResponseEntity<MemberProfileDTO> getProfile(@RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        Long resolvedUserId = resolveUserId(userIdHeader);
        if (resolvedUserId == null) {
            log.warn("缺少用户信息，无法返回会员数据");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(memberService.getMemberProfile(resolvedUserId));
    }

    /**
     * 兼容两种获取用户 ID 的方式：
     * 1) 前端 request() 已经带上的 X-User-Id
     * 2) 已登录的 JWT（Admin 作为认证主体）
     */
    private Long resolveUserId(String userIdHeader) {
        if (userIdHeader != null && !userIdHeader.isBlank()) {
            return Long.valueOf(userIdHeader);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Admin admin) {
            return admin.getId();
        }
        return null;
    }
}
