import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/forum',
    name: 'Forum',
    component: () => import('@/views/forum/Index.vue'),
    meta: { title: '校园论坛' }
  },
  {
    path: '/forum/:id',
    name: 'ForumDetail',
    component: () => import('@/views/forum/Detail.vue'),
    meta: { title: '帖子详情' }
  },
  {
    path: '/forum/post',
    name: 'ForumPost',
    component: () => import('@/views/forum/Post.vue'),
    meta: { title: '发布帖子', requiresAuth: true }
  },
  {
    path: '/market',
    name: 'Market',
    component: () => import('@/views/market/Index.vue'),
    meta: { title: '校园市场' }
  },
  {
    path: '/market/:id',
    name: 'MarketDetail',
    component: () => import('@/views/market/Detail.vue'),
    meta: { title: '商品详情' }
  },
  {
    path: '/market/post',
    name: 'MarketPost',
    component: () => import('@/views/market/Post.vue'),
    meta: { title: '发布商品', requiresAuth: true }
  },
  {
    path: '/job',
    name: 'Job',
    component: () => import('@/views/job/Index.vue'),
    meta: { title: '校园兼职' }
  },
  {
    path: '/job/:id',
    name: 'JobDetail',
    component: () => import('@/views/job/Detail.vue'),
    meta: { title: '兼职详情' }
  },
  {
    path: '/job/post',
    name: 'JobPost',
    component: () => import('@/views/job/Post.vue'),
    meta: { title: '发布兼职', requiresAuth: true }
  },
  {
    path: '/ai-assistant',
    name: 'AIAssistant',
    component: () => import('@/views/ai-assistant/Index.vue'),
    meta: { title: 'AI学长' }
  },
  {
    path: '/user/:id',
    name: 'UserProfile',
    component: () => import('@/views/user/Profile.vue'),
    meta: { title: '个人主页' }
  },
  {
    path: '/user/settings',
    name: 'UserSettings',
    component: () => import('@/views/user/Settings.vue'),
    meta: { title: '个人设置', requiresAuth: true }
  },
  {
    path: '/content',
    name: 'ContentManage',
    component: () => import('@/views/content/Index.vue'),
    meta: { title: '内容管理', requiresAuth: true }
  },
  {
    path: '/message',
    name: 'Message',
    component: () => import('@/views/message/Index.vue'),
    meta: { title: '消息中心', requiresAuth: true }
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/views/Search.vue'),
    meta: { title: '搜索结果' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { title: '注册' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else if (to.hash) {
      return { el: to.hash, behavior: 'smooth' }
    } else {
      return { top: 0 }
    }
  }
})

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title || '莆田学院校园生活系统'} - 莆田学院`
  next()
})

export default router
