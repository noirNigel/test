import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import App from './App.vue';
import router from './router';

const app = createApp(App);
app.use(ElementPlus);
app.use(router);

// 暴露 router 便于在控制台调试（开发用）
window.__APP_ROUTER__ = router;

// 初始化（不阻塞挂载）：进行重定向但始终挂载应用
const init = () => {
    const token = localStorage.getItem('token');
    const currentPath = window.location.pathname;

    console.log('初始化检查:', { token: !!token, currentPath });

    // 如果没有 token 且不在登录页，导航到登录页（但仍然挂载 app）
    if (!token && currentPath !== '/login' && currentPath !== '/') {
        console.log('未登录，导航到登录页');
        router.replace('/login').catch(() => {});
    }

    // 如果有 token 且在登录页，导航到仪表盘
    if (token && (currentPath === '/login' || currentPath === '/')) {
        console.log('已登录，导航到仪表盘');
        router.replace('/admin/dashboard').catch(() => {});
    }

    // 始终挂载 app（避免因未挂载导致 UI/路由不可用）
    app.mount('#app');
};

init();
