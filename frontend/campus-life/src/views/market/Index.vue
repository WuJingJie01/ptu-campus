<template>
  <MainLayout>
    <div class="market-page">
      <div class="market-container">
        <div class="market-main">
          <div class="market-header">
            <div class="market-filters">
              <div class="filter-group">
                <label>分类：</label>
                <div class="filter-options">
                  <button
                    v-for="category in categories"
                    :key="category.value"
                    :class="['filter-btn', { 'filter-btn-active': activeCategory === category.value }]"
                    @click="activeCategory = category.value"
                  >
                    {{ category.label }}
                  </button>
                </div>
              </div>
              <div class="filter-group">
                <label>状态：</label>
                <div class="filter-options">
                  <button
                    v-for="status in statusOptions"
                    :key="status.value"
                    :class="['filter-btn', { 'filter-btn-active': activeStatus === status.value }]"
                    @click="activeStatus = status.value"
                  >
                    {{ status.label }}
                  </button>
                </div>
              </div>
            </div>
            <Button type="primary" @click="router.push('/market/post')">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              发布商品
            </Button>
          </div>

          <div class="market-grid">
            <div
              v-for="product in products"
              :key="product.id"
              class="product-card"
              @click="router.push(`/market/${product.id}`)"
            >
              <div class="product-image">
                <img :src="product.image" :alt="product.title" />
                <Badge
                  v-if="product.status !== 'selling'"
                  :type="product.status === 'sold' ? 'default' : 'warning'"
                  class="product-status"
                >
                  {{ statusMap[product.status] }}
                </Badge>
              </div>
              <div class="product-info">
                <h3 class="product-title">{{ product.title }}</h3>
                <div class="product-price">¥{{ product.price }}</div>
                <div class="product-meta">
                  <div class="product-seller">
                    <Avatar :src="product.seller.avatar" :name="product.seller.name" size="xs" />
                    <span>{{ product.seller.name }}</span>
                  </div>
                  <span class="product-time">{{ product.time }}</span>
                </div>
              </div>
            </div>
          </div>

          <div class="market-pagination">
            <button class="page-btn" :disabled="currentPage === 1" @click="currentPage--">
              上一页
            </button>
            <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
            <button class="page-btn" :disabled="currentPage === totalPages" @click="currentPage++">
              下一页
            </button>
          </div>
        </div>

        <aside class="market-sidebar">
          <Card title="💰 热门商品" class="sidebar-card">
            <div class="hot-list">
              <div
                v-for="item in hotProducts"
                :key="item.id"
                class="hot-item"
                @click="router.push(`/market/${item.id}`)"
              >
                <img :src="item.image" :alt="item.title" class="hot-image" />
                <div class="hot-info">
                  <h4 class="hot-title">{{ item.title }}</h4>
                  <div class="hot-price">¥{{ item.price }}</div>
                </div>
              </div>
            </div>
          </Card>

          <Card title="📢 交易须知" class="sidebar-card">
            <ul class="notice-list">
              <li>请在校园内当面交易</li>
              <li>交易前请核实商品信息</li>
              <li>建议使用平台担保交易</li>
              <li>遇到问题请及时举报</li>
            </ul>
          </Card>
        </aside>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Button, Avatar, Badge, Card } from '@/components'

const router = useRouter()
const activeCategory = ref('all')
const activeStatus = ref('all')
const currentPage = ref(1)
const totalPages = ref(5)

const categories = [
  { label: '全部', value: 'all' },
  { label: '数码电子', value: 'digital' },
  { label: '书籍教材', value: 'books' },
  { label: '生活用品', value: 'daily' },
  { label: '服装鞋帽', value: 'clothes' },
  { label: '运动户外', value: 'sports' },
  { label: '其他', value: 'other' }
]

const statusOptions = [
  { label: '全部', value: 'all' },
  { label: '在售', value: 'selling' },
  { label: '已售', value: 'sold' },
  { label: '预订', value: 'reserved' }
]

const statusMap: Record<string, string> = {
  selling: '在售',
  sold: '已售',
  reserved: '预订中'
}

const products = ref([
  {
    id: 1,
    title: '九成新 iPad Pro 11寸 256G WiFi版',
    price: 4500,
    image: 'https://picsum.photos/300/300?random=10',
    status: 'selling',
    seller: { name: '陈同学', avatar: '' },
    time: '刚刚'
  },
  {
    id: 2,
    title: '高等数学同济第七版 上下册',
    price: 25,
    image: 'https://picsum.photos/300/300?random=11',
    status: 'selling',
    seller: { name: '刘同学', avatar: '' },
    time: '5分钟前'
  },
  {
    id: 3,
    title: '小米台灯 护眼LED 无频闪',
    price: 45,
    image: 'https://picsum.photos/300/300?random=12',
    status: 'selling',
    seller: { name: '周同学', avatar: '' },
    time: '10分钟前'
  },
  {
    id: 4,
    title: '捷安特山地车 九成新',
    price: 600,
    image: 'https://picsum.photos/300/300?random=13',
    status: 'reserved',
    seller: { name: '吴同学', avatar: '' },
    time: '30分钟前'
  },
  {
    id: 5,
    title: 'AirPods Pro 2代 全新未拆封',
    price: 1400,
    image: 'https://picsum.photos/300/300?random=14',
    status: 'selling',
    seller: { name: '郑同学', avatar: '' },
    time: '1小时前'
  },
  {
    id: 6,
    title: '考研英语真题黄皮书 2015-2024',
    price: 35,
    image: 'https://picsum.photos/300/300?random=15',
    status: 'sold',
    seller: { name: '孙同学', avatar: '' },
    time: '2小时前'
  }
])

const hotProducts = [
  { id: 1, title: 'iPad Pro 11寸', price: 4500, image: 'https://picsum.photos/80/80?random=20' },
  { id: 2, title: 'AirPods Pro 2代', price: 1400, image: 'https://picsum.photos/80/80?random=21' },
  { id: 3, title: '捷安特山地车', price: 600, image: 'https://picsum.photos/80/80?random=22' }
]
</script>

<style lang="scss" scoped>
.market-page {
  min-height: 100vh;
  padding: $spacing-lg 0;
  background: $bg-secondary;
}

.market-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 $spacing-lg;
  display: grid;
  grid-template-columns: 1fr;
  gap: $spacing-xl;
  
  @include respond-to(lg) {
    grid-template-columns: 1fr 300px;
  }
}

.market-header {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
  
  @include respond-to(md) {
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-start;
  }
}

.market-filters {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.filter-group {
  display: flex;
  align-items: flex-start;
  gap: $spacing-sm;
  
  label {
    font-size: $font-size-sm;
    color: $text-muted;
    padding-top: $spacing-sm;
    white-space: nowrap;
  }
}

.filter-options {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-xs;
}

.filter-btn {
  padding: $spacing-xs $spacing-md;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: $bg-tertiary;
  border: none;
  border-radius: $radius-full;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    color: $primary-color;
    background: $primary-bg;
  }
  
  &-active {
    color: $text-white;
    background: $primary-color;
  }
}

.market-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-md;
  
  @include respond-to(sm) {
    grid-template-columns: repeat(3, 1fr);
  }
}

.product-card {
  background: $bg-primary;
  border-radius: $radius-lg;
  overflow: hidden;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: $shadow-lg;
  }
}

.product-image {
  position: relative;
  aspect-ratio: 1;
  background: $bg-tertiary;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.product-status {
  position: absolute;
  top: $spacing-sm;
  left: $spacing-sm;
}

.product-info {
  padding: $spacing-md;
}

.product-title {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-sm;
  
  @include truncate(2);
}

.product-price {
  font-size: $font-size-lg;
  font-weight: $font-weight-bold;
  color: $danger-color;
  margin-bottom: $spacing-sm;
}

.product-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.product-seller {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  font-size: $font-size-xs;
  color: $text-muted;
}

.product-time {
  font-size: $font-size-xs;
  color: $text-light;
}

.market-pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: $spacing-md;
  margin-top: $spacing-xl;
}

.page-btn {
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: $bg-primary;
  border: 1px solid $border-color;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover:not(:disabled) {
    color: $primary-color;
    border-color: $primary-color;
  }
  
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}

.page-info {
  font-size: $font-size-sm;
  color: $text-muted;
}

.market-sidebar {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.sidebar-card {
  margin-bottom: 0;
}

.hot-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.hot-item {
  display: flex;
  gap: $spacing-md;
  padding: $spacing-sm;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
}

.hot-image {
  width: 60px;
  height: 60px;
  border-radius: $radius-md;
  object-fit: cover;
  background: $bg-tertiary;
}

.hot-info {
  flex: 1;
}

.hot-title {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
  
  @include truncate(2);
}

.hot-price {
  font-size: $font-size-sm;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

.notice-list {
  padding-left: $spacing-lg;
  margin: 0;
  
  li {
    font-size: $font-size-sm;
    color: $text-secondary;
    line-height: $line-height-relaxed;
    margin-bottom: $spacing-sm;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
