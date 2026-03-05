<template>
  <MainLayout>
    <div class="detail-page">
      <div class="detail-container">
        <div class="detail-main">
          <Card class="product-card">
            <div class="product-gallery">
              <div class="product-main-image">
                <img :src="product.images[activeImage]" :alt="product.title" />
              </div>
              <div class="product-thumbnails">
                <button
                  v-for="(image, index) in product.images"
                  :key="index"
                  :class="['thumbnail', { 'thumbnail-active': activeImage === index }]"
                  @click="activeImage = index"
                >
                  <img :src="image" :alt="`图片${index + 1}`" />
                </button>
              </div>
            </div>

            <div class="product-content">
              <div class="product-header">
                <h1 class="product-title">{{ product.title }}</h1>
                <Badge :type="product.status === 'selling' ? 'success' : 'default'">
                  {{ statusMap[product.status] }}
                </Badge>
              </div>

              <div class="product-price-row">
                <div class="product-price">¥{{ product.price }}</div>
                <div class="product-original-price" v-if="product.originalPrice">
                  原价 ¥{{ product.originalPrice }}
                </div>
              </div>

              <div class="product-info-list">
                <div class="product-info-item">
                  <span class="info-label">发布者</span>
                  <div class="info-value seller-info" @click="router.push(`/user/${product.seller.id}`)">
                    <Avatar :src="product.seller.avatar" :name="product.seller.name" size="sm" />
                    <span>{{ product.seller.name }}</span>
                    <Badge v-if="product.seller.verified" type="primary" size="sm">已认证</Badge>
                  </div>
                </div>
                <div class="product-info-item">
                  <span class="info-label">发布时间</span>
                  <span class="info-value">{{ product.time }}</span>
                </div>
                <div class="product-info-item">
                  <span class="info-label">交易方式</span>
                  <span class="info-value">{{ product.tradeMethod }}</span>
                </div>
                <div class="product-info-item" v-if="product.seller.college">
                  <span class="info-label">卖家信息</span>
                  <span class="info-value">{{ product.seller.college }} · {{ product.seller.grade }}</span>
                </div>
              </div>

              <div class="product-actions">
                <Button type="primary" size="lg" @click="handleBuy" :disabled="product.status !== 'selling'">
                  {{ product.status === 'selling' ? '立即购买' : '已售出' }}
                </Button>
                <Button type="outline" size="lg" @click="handleMessage">
                  私信卖家
                </Button>
                <button class="action-icon-btn" :class="{ 'action-icon-btn-active': product.collected }" @click="handleCollect">
                  <svg width="20" height="20" viewBox="0 0 24 24" :fill="product.collected ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                    <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z" />
                  </svg>
                </button>
                <button class="action-icon-btn" @click="handleShare">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="18" cy="5" r="3" />
                    <circle cx="6" cy="12" r="3" />
                    <circle cx="18" cy="19" r="3" />
                    <line x1="8.59" y1="13.51" x2="15.42" y2="17.49" />
                    <line x1="15.41" y1="6.51" x2="8.59" y2="10.49" />
                  </svg>
                </button>
                <button class="action-icon-btn" @click="showReportModal = true">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z" />
                    <line x1="4" y1="22" x2="4" y2="15" />
                  </svg>
                </button>
              </div>
            </div>
          </Card>

          <Card title="商品描述" class="description-card">
            <div class="product-description">
              <p v-for="(para, index) in product.description" :key="index">{{ para }}</p>
            </div>
          </Card>

          <Card title="卖家评价" class="reviews-card">
            <div class="reviews-list">
              <div v-for="review in reviews" :key="review.id" class="review-item">
                <div class="review-header">
                  <Avatar :src="review.user.avatar" :name="review.user.name" size="sm" />
                  <span class="review-user">{{ review.user.name }}</span>
                  <span class="review-time">{{ review.time }}</span>
                </div>
                <p class="review-content">{{ review.content }}</p>
              </div>
            </div>
          </Card>
        </div>

        <aside class="detail-sidebar">
          <Card title="卖家信息" class="sidebar-card">
            <div class="seller-card">
              <Avatar :src="product.seller.avatar" :name="product.seller.name" size="xl" />
              <h4 class="seller-name">{{ product.seller.name }}</h4>
              <div class="seller-stats">
                <div class="seller-stat">
                  <span class="seller-stat-value">{{ product.seller.soldCount }}</span>
                  <span class="seller-stat-label">已售</span>
                </div>
                <div class="seller-stat">
                  <span class="seller-stat-value">{{ product.seller.rating }}</span>
                  <span class="seller-stat-label">好评率</span>
                </div>
              </div>
              <Button type="primary" block @click="router.push(`/user/${product.seller.id}`)">
                查看主页
              </Button>
            </div>
          </Card>

          <Card title="相关推荐" class="sidebar-card">
            <div class="related-list">
              <div
                v-for="item in relatedProducts"
                :key="item.id"
                class="related-item"
                @click="router.push(`/market/${item.id}`)"
              >
                <img :src="item.image" :alt="item.title" class="related-image" />
                <div class="related-info">
                  <h5 class="related-title">{{ item.title }}</h5>
                  <div class="related-price">¥{{ item.price }}</div>
                </div>
              </div>
            </div>
          </Card>
        </aside>
      </div>
    </div>

    <Modal v-model="showReportModal" title="举报商品" size="sm">
      <div class="report-form">
        <div class="report-options">
          <label v-for="option in reportOptions" :key="option" class="report-option">
            <input type="radio" v-model="reportType" :value="option" />
            <span>{{ option }}</span>
          </label>
        </div>
        <textarea
          v-model="reportReason"
          placeholder="请详细描述举报原因..."
          rows="4"
          class="report-textarea"
        ></textarea>
      </div>
      <template #footer>
        <Button type="ghost" @click="showReportModal = false">取消</Button>
        <Button type="primary" :disabled="!reportType || !reportReason" @click="submitReport">
          提交举报
        </Button>
      </template>
    </Modal>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Card, Button, Avatar, Badge, Modal } from '@/components'

const router = useRouter()
const activeImage = ref(0)
const showReportModal = ref(false)
const reportType = ref('')
const reportReason = ref('')

const reportOptions = ['虚假信息', '价格欺诈', '商品已售', '广告', '其他']

const statusMap: Record<string, string> = {
  selling: '在售',
  sold: '已售',
  reserved: '预订中'
}

const product = ref({
  id: 1,
  title: '九成新 iPad Pro 11寸 256G WiFi版 深空灰色',
  price: 4500,
  originalPrice: 6199,
  status: 'selling',
  images: [
    'https://picsum.photos/600/600?random=30',
    'https://picsum.photos/600/600?random=31',
    'https://picsum.photos/600/600?random=32'
  ],
  description: [
    'iPad Pro 11寸第三代，2022年购买，使用一年左右。',
    '屏幕完美无划痕，机身有轻微使用痕迹，整体成色95新。',
    '电池健康度92%，续航正常。配件有原装充电器和数据线。',
    '因换新出售，功能完好无任何问题，可当面验货。',
    '仅限校内交易，支持图书馆或食堂当面交易。'
  ],
  tradeMethod: '校内当面交易',
  time: '2024-03-15 14:30',
  seller: {
    id: 1,
    name: '陈同学',
    avatar: '',
    verified: true,
    college: '计算机学院',
    grade: '2021级',
    soldCount: 12,
    rating: '98%'
  },
  collected: false
})

const reviews = ref([
  {
    id: 1,
    user: { name: '李同学', avatar: '' },
    content: '卖家很靠谱，商品描述和实物一致，交易很顺利！',
    time: '3天前'
  },
  {
    id: 2,
    user: { name: '王同学', avatar: '' },
    content: '态度很好，耐心解答问题，推荐购买！',
    time: '1周前'
  }
])

const relatedProducts = ref([
  { id: 2, title: 'AirPods Pro 2代', price: 1400, image: 'https://picsum.photos/80/80?random=40' },
  { id: 3, title: 'iPad 保护壳', price: 68, image: 'https://picsum.photos/80/80?random=41' },
  { id: 4, title: 'Apple Pencil 2代', price: 650, image: 'https://picsum.photos/80/80?random=42' }
])

const handleBuy = () => {
  alert('购买成功，请等待卖家确认')
}

const handleMessage = () => {
  router.push('/message')
}

const handleCollect = () => {
  product.value.collected = !product.value.collected
}

const handleShare = () => {
  navigator.clipboard.writeText(window.location.href)
  alert('链接已复制到剪贴板')
}

const submitReport = () => {
  alert('举报已提交，等待审核')
  showReportModal.value = false
}
</script>

<style lang="scss" scoped>
.detail-page {
  min-height: 100vh;
  padding: $spacing-lg 0;
  background: $bg-secondary;
}

.detail-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 $spacing-lg;
  display: grid;
  grid-template-columns: 1fr;
  gap: $spacing-xl;
  
  @include respond-to(lg) {
    grid-template-columns: 1fr 320px;
  }
}

.product-card {
  margin-bottom: 0;
}

.product-gallery {
  margin-bottom: $spacing-xl;
}

.product-main-image {
  aspect-ratio: 1;
  background: $bg-tertiary;
  border-radius: $radius-lg;
  overflow: hidden;
  margin-bottom: $spacing-md;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.product-thumbnails {
  display: flex;
  gap: $spacing-sm;
}

.thumbnail {
  width: 60px;
  height: 60px;
  border-radius: $radius-md;
  overflow: hidden;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all $transition-fast;
  
  &-active {
    border-color: $primary-color;
  }
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.product-content {
  padding-top: $spacing-lg;
  border-top: 1px solid $border-color;
}

.product-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: $spacing-md;
  margin-bottom: $spacing-md;
}

.product-title {
  font-size: $font-size-xl;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  line-height: 1.4;
  margin: 0;
}

.product-price-row {
  display: flex;
  align-items: baseline;
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
}

.product-price {
  font-size: $font-size-2xl;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

.product-original-price {
  font-size: $font-size-sm;
  color: $text-muted;
  text-decoration: line-through;
}

.product-info-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-tertiary;
  border-radius: $radius-lg;
  margin-bottom: $spacing-lg;
}

.product-info-item {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.info-label {
  width: 80px;
  font-size: $font-size-sm;
  color: $text-muted;
  flex-shrink: 0;
}

.info-value {
  font-size: $font-size-sm;
  color: $text-primary;
}

.seller-info {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  cursor: pointer;
  
  &:hover {
    color: $primary-color;
  }
}

.product-actions {
  display: flex;
  gap: $spacing-md;
  flex-wrap: wrap;
}

.action-icon-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  color: $text-muted;
  background: $bg-tertiary;
  border: none;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    color: $primary-color;
    background: $primary-bg;
  }
  
  &-active {
    color: $primary-color;
  }
}

.description-card,
.reviews-card {
  margin-top: $spacing-lg;
}

.product-description {
  p {
    font-size: $font-size-base;
    color: $text-secondary;
    line-height: $line-height-relaxed;
    margin: 0 0 $spacing-md;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}

.reviews-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.review-item {
  padding: $spacing-md;
  background: $bg-tertiary;
  border-radius: $radius-lg;
}

.review-header {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  margin-bottom: $spacing-sm;
}

.review-user {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
}

.review-time {
  font-size: $font-size-xs;
  color: $text-light;
  margin-left: auto;
}

.review-content {
  font-size: $font-size-sm;
  color: $text-secondary;
  margin: 0;
}

.detail-sidebar {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.sidebar-card {
  margin-bottom: 0;
}

.seller-card {
  text-align: center;
}

.seller-name {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: $spacing-md 0 $spacing-sm;
}

.seller-stats {
  display: flex;
  justify-content: center;
  gap: $spacing-xl;
  margin-bottom: $spacing-md;
}

.seller-stat {
  text-align: center;
}

.seller-stat-value {
  display: block;
  font-size: $font-size-lg;
  font-weight: $font-weight-bold;
  color: $text-primary;
}

.seller-stat-label {
  font-size: $font-size-xs;
  color: $text-muted;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.related-item {
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

.related-image {
  width: 60px;
  height: 60px;
  border-radius: $radius-md;
  object-fit: cover;
  background: $bg-tertiary;
}

.related-info {
  flex: 1;
}

.related-title {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
  
  @include truncate(2);
}

.related-price {
  font-size: $font-size-sm;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

.report-form {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.report-options {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.report-option {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: $font-size-sm;
  color: $text-secondary;
  cursor: pointer;
}

.report-textarea {
  width: 100%;
  padding: $spacing-md;
  font-size: $font-size-sm;
  background: $bg-tertiary;
  border: 1px solid transparent;
  border-radius: $radius-lg;
  resize: none;
  outline: none;
  
  &:focus {
    background: $bg-primary;
    border-color: $primary-color;
  }
}
</style>
