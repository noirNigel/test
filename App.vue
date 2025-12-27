<template>
  <div id="app">
    <!-- 只有在登录状态且当前路由不是登录页时才显示完整布局 -->
    <template v-if="isLoggedIn && $route.path !== '/login'">
      <el-container class="app-container">
        <el-header class="app-header">
          <div class="header-content">
            <div class="logo">
              <h2>后台管理系统</h2>
            </div>
            <div class="user-info">
              <el-dropdown>
                <span class="el-dropdown-link">
                  管理员
                  <el-icon><ArrowDown /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleLogout">
                      退出登录
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </el-header>

        <el-container>
          <el-aside width="200px" class="app-sidebar">
            <el-menu
                :default-active="$route.path"
                router
                class="sidebar-menu"
            >
              <!-- 仪表盘 -->
              <el-menu-item index="/admin/dashboard">
                <el-icon><Odometer /></el-icon>
                <span>仪表盘</span>
              </el-menu-item>

              <!-- 商品管理 -->
              <el-sub-menu index="product">
                <template #title>
                  <el-icon><Goods /></el-icon>
                  <span>商品管理</span>
                </template>
                <el-menu-item index="/admin/products">商品列表</el-menu-item>
                <el-menu-item index="/admin/categories">分类管理</el-menu-item>
                <el-menu-item index="/admin/inventory">库存管理</el-menu-item>
              </el-sub-menu>

              <!-- 订单管理 -->
              <el-sub-menu index="order">
                <template #title>
                  <el-icon><List /></el-icon>
                  <span>订单管理</span>
                </template>
                <el-menu-item index="/admin/orders">订单列表</el-menu-item>
                <el-menu-item index="/admin/refunds">售后处理</el-menu-item>
                <el-menu-item index="/admin/print-settings">打印设置</el-menu-item>
              </el-sub-menu>

              <!-- 营销活动 -->
              <el-sub-menu index="marketing">
                <template #title>
                  <el-icon><Promotion /></el-icon>
                  <span>营销活动</span>
                </template>
                <el-menu-item index="/admin/marketing/coupons">
                  优惠券管理
                </el-menu-item>
                <el-menu-item index="/admin/marketing/member-levels">
                  会员等级
                </el-menu-item>
                <el-menu-item index="/admin/marketing/promotions">
                  促销活动
                </el-menu-item>
                <el-menu-item index="/admin/marketing/points-mall">
                  积分商城
                </el-menu-item>
              </el-sub-menu>

              <!-- 系统管理（新增，对应 /system/... 路由） -->
              <el-sub-menu index="system">
                <template #title>
                  <el-icon><Setting /></el-icon>
                  <span>系统管理</span>
                </template>
                <el-menu-item index="/system/admins">员工管理</el-menu-item>
                <el-menu-item index="/system/stores">门店管理</el-menu-item>
                <el-menu-item index="/system/config">系统设置</el-menu-item>
                <el-menu-item index="/system/logs">操作日志</el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-aside>

          <el-main class="app-main">
            <router-view />
          </el-main>
        </el-container>
      </el-container>
    </template>

    <!-- 未登录时只显示路由视图 -->
    <router-view v-else />
  </div>
</template>

<script setup>
import { onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuth } from '@/composables/useAuth'
import {
  Odometer,
  Goods,
  List,
  ArrowDown,
  Promotion,
  Setting
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const { isLoggedIn, updateLoginStatus } = useAuth()

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      type: 'warning'
    })

    localStorage.removeItem('token')
    updateLoginStatus() // 立即更新状态

    router.push('/login')
  } catch (error) {
    // 用户取消退出
  }
}

// 监听路由变化
watch(
    () => route.path,
    newPath => {
      console.log('Route changed to:', newPath)
      updateLoginStatus() // 更新登录状态

      const token = localStorage.getItem('token')

      if (!token && newPath !== '/login' && newPath !== '/') {
        router.replace('/login')
      }

      if (token && newPath === '/login') {
        router.replace('/admin/dashboard')
      }
    },
    { immediate: true }
)

onMounted(() => {
  console.log('App mounted, login status:', isLoggedIn.value)
  updateLoginStatus() // 初始状态检查
})
</script>

<style scoped>
#app {
  height: 100vh;
}

.app-container {
  height: 100%;
}

.app-header {
  background-color: #409eff;
  color: white;
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.logo h2 {
  margin: 0;
  color: white;
}

.user-info {
  color: white;
}

.app-sidebar {
  background-color: #fff;
  border-right: 1px solid #e6e6e6;
}

.sidebar-menu {
  border: none;
  height: 100%;
}

.app-main {
  padding: 0;
  background-color: #f5f7fa;
}

.el-dropdown-link {
  color: white;
  cursor: pointer;
}
</style>
