<template>
  <MainLayout>
    <div class="content-page">
      <div class="content-container">
        <aside class="content-sidebar">
          <nav class="content-nav">
            <button
              v-for="item in navItems"
              :key="item.value"
              :class="['nav-item', { 'nav-item-active': activeNav === item.value }]"
              @click="activeNav = item.value"
            >
              <span v-html="item.icon"></span>
              <span>{{ item.label }}</span>
            </button>
          </nav>
        </aside>

        <main class="content-main">
          <div class="main-header">
            <h2>{{ currentNav?.label }}</h2>
            <Button type="primary" @click="handleCreate">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              发布新{{ currentNav?.label.slice(0, 2) }}
            </Button>
          </div>

          <div v-if="activeNav === 'posts'" class="content-list">
            <div v-for="item in posts" :key="item.id" class="content-item">
              <div class="item-info">
                <h4>{{ item.title }}</h4>
                <div class="item-meta">
                  <span>{{ item.time }}</span>
                  <Badge :type="getStatusType(item.status)">{{ getStatusText(item.status) }}</Badge>
                </div>
              </div>
              <div class="item-actions">
                <Button type="ghost" size="sm" @click="handleEdit('post', item.id)">编辑</Button>
                <Button type="ghost" size="sm" @click="handleDelete(item)">删除</Button>
                <Button v-if="item.status === 'draft'" type="primary" size="sm" @click="handleSubmit(item)">提交审核</Button>
              </div>
            </div>
          </div>

          <div v-else-if="activeNav === 'products'" class="content-list">
            <div v-for="item in products" :key="item.id" class="content-item">
              <div class="item-info">
                <h4>{{ item.title }}</h4>
                <div class="item-meta">
                  <span class="item-price">¥{{ item.price }}</span>
                  <span>{{ item.time }}</span>
                  <Badge :type="getStatusType(item.status)">{{ getStatusText(item.status) }}</Badge>
                </div>
              </div>
              <div class="item-actions">
                <Button type="ghost" size="sm" @click="handleEdit('product', item.id)">编辑</Button>
                <Button type="ghost" size="sm" @click="handleDelete(item)">删除</Button>
                <Button v-if="item.status === 'draft'" type="primary" size="sm" @click="handleSubmit(item)">提交审核</Button>
              </div>
            </div>
          </div>

          <div v-else-if="activeNav === 'jobs'" class="content-list">
            <div v-for="item in jobs" :key="item.id" class="content-item">
              <div class="item-info">
                <h4>{{ item.title }}</h4>
                <div class="item-meta">
                  <span class="item-salary">{{ item.salary }}</span>
                  <span>{{ item.time }}</span>
                  <Badge :type="getStatusType(item.status)">{{ getStatusText(item.status) }}</Badge>
                </div>
              </div>
              <div class="item-actions">
                <Button type="ghost" size="sm" @click="handleEdit('job', item.id)">编辑</Button>
                <Button type="ghost" size="sm" @click="handleDelete(item)">删除</Button>
                <Button v-if="item.status === 'draft'" type="primary" size="sm" @click="handleSubmit(item)">提交审核</Button>
              </div>
            </div>
          </div>

          <div v-else-if="activeNav === 'orders'" class="order-list">
            <div class="order-tabs">
              <button
                v-for="tab in orderTabs"
                :key="tab.value"
                :class="['order-tab', { 'order-tab-active': activeOrderTab === tab.value }]"
                @click="activeOrderTab = tab.value"
              >
                {{ tab.label }}
              </button>
            </div>
            <div class="orders">
              <div v-for="order in filteredOrders" :key="order.id" class="order-item">
                <div class="order-header">
                  <span class="order-id">订单号: {{ order.id }}</span>
                  <span class="order-time">{{ order.time }}</span>
                  <Badge :type="getOrderStatusType(order.status)">{{ getOrderStatusText(order.status) }}</Badge>
                </div>
                <div class="order-content">
                  <img :src="order.product.image" :alt="order.product.title" />
                  <div class="order-info">
                    <h4>{{ order.product.title }}</h4>
                    <p>¥{{ order.product.price }}</p>
                    <p>交易对象: {{ order.counterparty }}</p>
                  </div>
                </div>
                <div class="order-actions">
                  <Button v-if="order.status === 'pending'" type="primary" size="sm">确认付款</Button>
                  <Button v-if="order.status === 'paid'" type="primary" size="sm">确认发货</Button>
                  <Button v-if="order.status === 'shipped'" type="primary" size="sm">确认收货</Button>
                  <Button v-if="order.status === 'completed'" type="outline" size="sm">评价</Button>
                  <Button v-if="['pending', 'paid'].includes(order.status)" type="ghost" size="sm">取消订单</Button>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Button, Badge } from '@/components'

const router = useRouter()
const activeNav = ref('posts')
const activeOrderTab = ref('all')

const navItems = [
  { label: '帖子信息', value: 'posts', icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" /></svg>' },
  { label: '商品信息', value: 'products', icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2" ry="2" /><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" /></svg>' },
  { label: '兼职信息', value: 'jobs', icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2" ry="2" /><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" /></svg>' },
  { label: '订单管理', value: 'orders', icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" /><polyline points="14 2 14 8 20 8" /></svg>' }
]

const orderTabs = [
  { label: '全部', value: 'all' },
  { label: '待付款', value: 'pending' },
  { label: '待交易', value: 'paid' },
  { label: '待发货', value: 'confirmed' },
  { label: '待收货', value: 'shipped' },
  { label: '已完成', value: 'completed' }
]

const currentNav = computed(() => navItems.find(n => n.value === activeNav.value))

const posts = ref([
  { id: 1, title: '关于图书馆自习室预约系统的使用心得分享', time: '2024-03-15', status: 'published' },
  { id: 2, title: '考研经验分享：如何高效备考数学', time: '2024-03-10', status: 'reviewing' },
  { id: 3, title: '周末约球！羽毛球爱好者集合', time: '2024-03-08', status: 'draft' }
])

const products = ref([
  { id: 1, title: 'iPad Pro 11寸', price: 4500, time: '2024-03-14', status: 'published' },
  { id: 2, title: '高等数学教材', price: 25, time: '2024-03-12', status: 'sold' }
])

const jobs = ref([
  { id: 1, title: '家教辅导（数学）', salary: '50元/小时', time: '2024-03-13', status: 'published' }
])

const orders = ref([
  { id: '202403150001', time: '2024-03-15 14:30', status: 'pending', counterparty: '陈同学', product: { title: 'iPad Pro 11寸', price: 4500, image: 'https://picsum.photos/100/100?random=60' } },
  { id: '202403140002', time: '2024-03-14 10:00', status: 'shipped', counterparty: '刘同学', product: { title: '高等数学教材', price: 25, image: 'https://picsum.photos/100/100?random=61' } }
])

const filteredOrders = computed(() => {
  if (activeOrderTab.value === 'all') return orders.value
  return orders.value.filter(o => o.status === activeOrderTab.value)
})

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    draft: 'default',
    reviewing: 'warning',
    published: 'success',
    sold: 'default'
  }
  return map[status] || 'default'
}

const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    draft: '未发布',
    reviewing: '审核中',
    published: '已发布',
    sold: '已售出'
  }
  return map[status] || status
}

const getOrderStatusType = (status: string) => {
  const map: Record<string, string> = {
    pending: 'warning',
    paid: 'info',
    confirmed: 'info',
    shipped: 'primary',
    completed: 'success',
    cancelled: 'default'
  }
  return map[status] || 'default'
}

const getOrderStatusText = (status: string) => {
  const map: Record<string, string> = {
    pending: '待付款',
    paid: '待交易',
    confirmed: '待发货',
    shipped: '待收货',
    completed: '已完成',
    cancelled: '已取消'
  }
  return map[status] || status
}

const handleCreate = () => {
  const routes: Record<string, string> = {
    posts: '/forum/post',
    products: '/market/post',
    jobs: '/job/post'
  }
  router.push(routes[activeNav.value] || '/')
}

const handleEdit = (type: string, id: number) => {
  console.log('Edit:', type, id)
}

const handleDelete = (item: any) => {
  if (confirm('确定要删除吗？')) {
    console.log('Delete:', item)
  }
}

const handleSubmit = (item: any) => {
  item.status = 'reviewing'
}
</script>

<style lang="scss" scoped>
.content-page {
  min-height: 100vh;
  background: $bg-secondary;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 220px 1fr;
  min-height: calc(100vh - 64px);
}

.content-sidebar {
  background: $bg-primary;
  border-right: 1px solid $border-color;
}

.content-nav {
  padding: $spacing-md;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  width: 100%;
  padding: $spacing-md;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: transparent;
  border: none;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
  
  &-active {
    color: $primary-color;
    background: $primary-bg;
  }
}

.content-main {
  padding: $spacing-lg;
}

.main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-lg;
  
  h2 {
    font-size: $font-size-xl;
    font-weight: $font-weight-semibold;
    color: $text-primary;
    margin: 0;
  }
}

.content-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.content-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  
  @include respond-to(sm) {
    flex-direction: column;
    align-items: flex-start;
    gap: $spacing-md;
  }
}

.item-info {
  h4 {
    font-size: $font-size-base;
    font-weight: $font-weight-medium;
    color: $text-primary;
    margin: 0 0 $spacing-xs;
  }
}

.item-meta {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  font-size: $font-size-sm;
  color: $text-muted;
}

.item-price,
.item-salary {
  color: $danger-color;
  font-weight: $font-weight-semibold;
}

.item-actions {
  display: flex;
  gap: $spacing-sm;
}

.order-tabs {
  display: flex;
  gap: $spacing-xs;
  margin-bottom: $spacing-lg;
  overflow-x: auto;
}

.order-tab {
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: $bg-tertiary;
  border: none;
  border-radius: $radius-full;
  cursor: pointer;
  white-space: nowrap;
  transition: all $transition-fast;
  
  &:hover {
    color: $primary-color;
  }
  
  &-active {
    color: $text-white;
    background: $primary-color;
  }
}

.orders {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.order-item {
  background: $bg-primary;
  border-radius: $radius-lg;
  overflow: hidden;
}

.order-header {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-md $spacing-lg;
  background: $bg-tertiary;
  font-size: $font-size-sm;
}

.order-id {
  color: $text-secondary;
}

.order-time {
  color: $text-muted;
}

.order-content {
  display: flex;
  gap: $spacing-md;
  padding: $spacing-lg;
  
  img {
    width: 80px;
    height: 80px;
    border-radius: $radius-md;
    object-fit: cover;
  }
}

.order-info {
  h4 {
    font-size: $font-size-base;
    font-weight: $font-weight-medium;
    color: $text-primary;
    margin: 0 0 $spacing-xs;
  }
  
  p {
    font-size: $font-size-sm;
    color: $text-secondary;
    margin: 0;
    
    &:first-of-type {
      color: $danger-color;
      font-weight: $font-weight-bold;
    }
  }
}

.order-actions {
  display: flex;
  gap: $spacing-sm;
  padding: 0 $spacing-lg $spacing-lg;
}
</style>
