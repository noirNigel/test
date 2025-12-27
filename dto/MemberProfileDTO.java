package org.example.demomanagementsystemcproject.dto;

import java.math.BigDecimal;
import java.util.List;

public class MemberProfileDTO {

    public static class UserSnapshot {
        private Long id;
        private Integer points;
        private Integer levelPoints;
        private Integer availablePoints;
        private BigDecimal balance;
        private String level;
        private String levelName;
        private Integer levelMinPoints;
        private String nextLevel;
        private Integer nextLevelMinPoints;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

        public Integer getLevelPoints() {
            return levelPoints;
        }

        public void setLevelPoints(Integer levelPoints) {
            this.levelPoints = levelPoints;
        }

        public Integer getAvailablePoints() {
            return availablePoints;
        }

        public void setAvailablePoints(Integer availablePoints) {
            this.availablePoints = availablePoints;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public Integer getLevelMinPoints() {
            return levelMinPoints;
        }

        public void setLevelMinPoints(Integer levelMinPoints) {
            this.levelMinPoints = levelMinPoints;
        }

        public String getNextLevel() {
            return nextLevel;
        }

        public void setNextLevel(String nextLevel) {
            this.nextLevel = nextLevel;
        }

        public Integer getNextLevelMinPoints() {
            return nextLevelMinPoints;
        }

        public void setNextLevelMinPoints(Integer nextLevelMinPoints) {
            this.nextLevelMinPoints = nextLevelMinPoints;
        }
    }

    private UserSnapshot user;
    private PointsRuleDTO rules;
    private List<MemberLevelDTO> levels;

    public UserSnapshot getUser() {
        return user;
    }

    public void setUser(UserSnapshot user) {
        this.user = user;
    }

    public PointsRuleDTO getRules() {
        return rules;
    }

    public void setRules(PointsRuleDTO rules) {
        this.rules = rules;
    }

    public List<MemberLevelDTO> getLevels() {
        return levels;
    }

    public void setLevels(List<MemberLevelDTO> levels) {
        this.levels = levels;
    }
}
