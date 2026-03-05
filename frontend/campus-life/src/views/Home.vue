<template>
  <MainLayout>
    <section class="hero">
      <div class="hero-container">
        <div class="hero-content">
          <h1 class="hero-title">
            欢迎来到
            <span class="hero-title-highlight">莆田学院</span>
            校园生活系统
          </h1>
          <p class="hero-desc">
            为莆田学院师生打造的一站式校园生活服务平台，集论坛交流、二手交易、兼职信息、智能问答于一体
          </p>
          <div class="hero-actions">
            <Button type="primary" size="lg" @click="router.push('/forum')">
              立即探索
            </Button>
            <Button type="outline" size="lg" @click="router.push('/login')">
              登录注册
            </Button>
          </div>
        </div>
        <div class="hero-illustration">
          <div class="hero-shapes">
            <div class="hero-shape hero-shape-1"></div>
            <div class="hero-shape hero-shape-2"></div>
            <div class="hero-shape hero-shape-3"></div>
          </div>
          <div class="hero-cards">
            <div class="hero-card hero-card-1">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
              </svg>
              <span>校园论坛</span>
            </div>
            <div class="hero-card hero-card-2">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2" />
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" />
              </svg>
              <span>二手交易</span>
            </div>
            <div class="hero-card hero-card-3">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2" />
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" />
              </svg>
              <span>校园兼职</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="features">
      <div class="container">
        <h2 class="section-title">核心功能</h2>
        <p class="section-desc">四大核心模块，满足校园生活多方位需求</p>
        
        <div class="features-grid">
          <div
            v-for="feature in features"
            :key="feature.title"
            class="feature-card"
            @click="router.push(feature.link)"
          >
            <div class="feature-icon" :style="{ background: feature.gradient }" v-html="feature.iconSvg">
            </div>
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-desc">{{ feature.desc }}</p>
            <div class="feature-arrow">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 12h14M12 5l7 7-7 7" />
              </svg>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="stats">
      <div class="container">
        <div class="stats-grid">
          <div v-for="stat in stats" :key="stat.label" class="stat-item">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <section class="recent">
      <div class="container">
        <div class="recent-header">
          <h2 class="section-title">最新动态</h2>
          <div class="recent-tabs">
            <button
              v-for="tab in recentTabs"
              :key="tab.value"
              :class="['recent-tab', { 'recent-tab-active': activeTab === tab.value }]"
              @click="activeTab = tab.value"
            >
              {{ tab.label }}
            </button>
          </div>
        </div>

        <div class="recent-content">
          <div v-if="activeTab === 'forum'" class="recent-list">
            <div v-for="post in recentPosts" :key="post.id" class="recent-item">
              <div class="recent-item-main">
                <Avatar :src="post.author.avatar" :name="post.author.name" size="md" />
                <div class="recent-item-content">
                  <h4 class="recent-item-title">{{ post.title }}</h4>
                  <div class="recent-item-meta">
                    <span>{{ post.author.name }}</span>
                    <span>·</span>
                    <span>{{ post.time }}</span>
                    <span>·</span>
                    <span>{{ post.category }}</span>
                  </div>
                </div>
              </div>
              <div class="recent-item-stats">
                <span>
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" />
                  </svg>
                  {{ post.likes }}
                </span>
                <span>
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
                  </svg>
                  {{ post.comments }}
                </span>
              </div>
            </div>
          </div>

          <div v-else-if="activeTab === 'market'" class="recent-grid">
            <div v-for="product in recentProducts" :key="product.id" class="product-card">
              <div class="product-image">
                <img :src="product.image" :alt="product.title" />
              </div>
              <div class="product-info">
                <h4 class="product-title">{{ product.title }}</h4>
                <div class="product-price">¥{{ product.price }}</div>
                <div class="product-meta">
                  <span>{{ product.seller }}</span>
                  <span>{{ product.time }}</span>
                </div>
              </div>
            </div>
          </div>

          <div v-else-if="activeTab === 'job'" class="recent-list">
            <div v-for="job in recentJobs" :key="job.id" class="job-item">
              <div class="job-main">
                <div class="job-info">
                  <h4 class="job-title">{{ job.title }}</h4>
                  <div class="job-meta">
                    <span class="job-salary">{{ job.salary }}</span>
                    <span>{{ job.location }}</span>
                    <span>{{ job.time }}</span>
                  </div>
                </div>
                <Badge :type="job.urgent ? 'danger' : 'default'">
                  {{ job.urgent ? '急招' : '招聘中' }}
                </Badge>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="cta">
      <div class="container">
        <div class="cta-content">
          <h2 class="cta-title">加入莆田学院校园生活社区</h2>
          <p class="cta-desc">与同学们一起分享校园生活的精彩瞬间</p>
          <Button type="primary" size="lg" @click="router.push('/register')">
            立即注册
          </Button>
        </div>
      </div>
    </section>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Button, Avatar, Badge } from '@/components'

const router = useRouter()
const activeTab = ref('forum')

const features = [
  {
    title: '校园论坛',
    desc: '分享校园生活、学习交流、失物招领，与同学们畅所欲言',
    link: '/forum',
    gradient: 'linear-gradient(135deg, #4A90D9, #6BA3E0)',
    iconSvg: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" /></svg>'
  },
  {
    title: '校园市场',
    desc: '二手物品交易、教材转让、生活用品，安全便捷的校园交易平台',
    link: '/market',
    gradient: 'linear-gradient(135deg, #5CB85C, #6ED06E)',
    iconSvg: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2" ry="2" /><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" /></svg>'
  },
  {
    title: '校园兼职',
    desc: '校内兼职、实习机会、勤工助学，丰富的校园工作信息',
    link: '/job',
    gradient: 'linear-gradient(135deg, #FF9500, #FFB347)',
    iconSvg: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2" ry="2" /><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" /></svg>'
  },
  {
    title: 'AI学长',
    desc: '智能问答助手，解答校园生活、学习、就业等各类问题',
    link: '/ai-assistant',
    gradient: 'linear-gradient(135deg, #9B59B6, #B07CC6)',
    iconSvg: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><circle cx="12" cy="12" r="10" /><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3" /><line x1="12" y1="17" x2="12.01" y2="17" /></svg>'
  }
]

const stats = [
  { value: '10,000+', label: '注册用户' },
  { value: '50,000+', label: '帖子数量' },
  { value: '8,000+', label: '交易完成' },
  { value: '2,000+', label: '兼职发布' }
]

const recentTabs = [
  { label: '最新帖子', value: 'forum' },
  { label: '最新商品', value: 'market' },
  { label: '最新兼职', value: 'job' }
]

const recentPosts = [
  {
    id: 1,
    title: '关于图书馆自习室预约系统的使用心得分享',
    author: { name: '张同学', avatar: '' },
    time: '10分钟前',
    category: '学习交流',
    likes: 128,
    comments: 32
  },
  {
    id: 2,
    title: '食堂新推出的菜品测评，强烈推荐！',
    author: { name: '李同学', avatar: '' },
    time: '30分钟前',
    category: '校园生活',
    likes: 256,
    comments: 48
  },
  {
    id: 3,
    title: '寻找丢失的校园卡，学号为2021XXXX',
    author: { name: '王同学', avatar: '' },
    time: '1小时前',
    category: '失物招领',
    likes: 45,
    comments: 12
  },
  {
    id: 4,
    title: '考研经验分享：如何高效备考数学',
    author: { name: '赵同学', avatar: '' },
    time: '2小时前',
    category: '学习交流',
    likes: 389,
    comments: 67
  }
]

const recentProducts = [
  {
    id: 1,
    title: '九成新iPad Pro 11寸 256G',
    price: 4500,
    image: 'https://picsum.photos/200/200?random=1',
    seller: '陈同学',
    time: '刚刚'
  },
  {
    id: 2,
    title: '高等数学同济第七版教材',
    price: 25,
    image: 'https://picsum.photos/200/200?random=2',
    seller: '刘同学',
    time: '5分钟前'
  },
  {
    id: 3,
    title: '小米台灯 护眼LED',
    price: 45,
    image: 'https://picsum.photos/200/200?random=3',
    seller: '周同学',
    time: '10分钟前'
  },
  {
    id: 4,
    title: '自行车 捷安特山地车',
    price: 600,
    image: 'https://picsum.photos/200/200?random=4',
    seller: '吴同学',
    time: '30分钟前'
  }
]

const recentJobs = [
  {
    id: 1,
    title: '图书馆助理管理员',
    salary: '15元/小时',
    location: '图书馆',
    time: '今天',
    urgent: true
  },
  {
    id: 2,
    title: '食堂窗口兼职',
    salary: '12元/小时',
    location: '第一食堂',
    time: '昨天',
    urgent: false
  },
  {
    id: 3,
    title: '家教辅导（数学）',
    salary: '50元/小时',
    location: '校内/线上',
    time: '昨天',
    urgent: false
  },
  {
    id: 4,
    title: '快递代取服务',
    salary: '3元/件',
    location: '校内快递点',
    time: '2天前',
    urgent: false
  }
]
</script>

<style lang="scss" scoped>
.hero {
  position: relative;
  padding: $spacing-2xl 0;
  background: linear-gradient(135deg, $primary-bg 0%, $bg-secondary 100%);
  overflow: hidden;
  
  @include respond-to(md) {
    padding: $spacing-2xl 0 80px;
  }
}

.hero-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 $spacing-lg;
  display: grid;
  grid-template-columns: 1fr;
  gap: $spacing-xl;
  align-items: center;
  
  @include respond-to(lg) {
    grid-template-columns: 1fr 1fr;
  }
}

.hero-content {
  text-align: center;
  
  @include respond-to(lg) {
    text-align: left;
  }
}

.hero-title {
  font-size: $font-size-3xl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  line-height: 1.2;
  margin: 0 0 $spacing-md;
  
  @include respond-to(md) {
    font-size: $font-size-4xl;
  }
  
  &-highlight {
    color: $primary-color;
  }
}

.hero-desc {
  font-size: $font-size-base;
  color: $text-secondary;
  line-height: $line-height-relaxed;
  margin: 0 0 $spacing-lg;
  
  @include respond-to(md) {
    font-size: $font-size-lg;
  }
}

.hero-actions {
  display: flex;
  gap: $spacing-md;
  justify-content: center;
  
  @include respond-to(lg) {
    justify-content: flex-start;
  }
}

.hero-illustration {
  position: relative;
  height: 300px;
  
  @include respond-to(md) {
    height: 400px;
  }
}

.hero-shapes {
  position: absolute;
  inset: 0;
}

.hero-shape {
  position: absolute;
  border-radius: 50%;
  
  &-1 {
    width: 200px;
    height: 200px;
    background: linear-gradient(135deg, rgba($primary-color, 0.2), rgba($primary-light, 0.1));
    top: 20%;
    left: 10%;
    animation: float 6s ease-in-out infinite;
  }
  
  &-2 {
    width: 150px;
    height: 150px;
    background: linear-gradient(135deg, rgba($secondary-color, 0.2), rgba($secondary-light, 0.1));
    top: 50%;
    right: 20%;
    animation: float 8s ease-in-out infinite;
    animation-delay: 1s;
  }
  
  &-3 {
    width: 100px;
    height: 100px;
    background: linear-gradient(135deg, rgba($accent-color, 0.2), rgba($accent-light, 0.1));
    bottom: 20%;
    left: 30%;
    animation: float 7s ease-in-out infinite;
    animation-delay: 2s;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

.hero-cards {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hero-card {
  position: absolute;
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-md $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  box-shadow: $shadow-lg;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-secondary;
  animation: cardFloat 4s ease-in-out infinite;
  
  &-1 {
    top: 15%;
    left: 5%;
    animation-delay: 0s;
  }
  
  &-2 {
    top: 40%;
    right: 5%;
    animation-delay: 0.5s;
  }
  
  &-3 {
    bottom: 15%;
    left: 20%;
    animation-delay: 1s;
  }
  
  svg {
    color: $primary-color;
  }
}

@keyframes cardFloat {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.features {
  padding: 80px 0;
  background: $bg-primary;
}

.section-title {
  font-size: $font-size-2xl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  text-align: center;
  margin: 0 0 $spacing-sm;
}

.section-desc {
  font-size: $font-size-base;
  color: $text-muted;
  text-align: center;
  margin: 0 0 $spacing-xl;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: $spacing-lg;
  
  @include respond-to(sm) {
    grid-template-columns: repeat(2, 1fr);
  }
  
  @include respond-to(lg) {
    grid-template-columns: repeat(4, 1fr);
  }
}

.feature-card {
  position: relative;
  padding: $spacing-xl;
  background: $bg-secondary;
  border-radius: $radius-xl;
  cursor: pointer;
  transition: all $transition-base;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: $shadow-lg;
    
    .feature-arrow {
      opacity: 1;
      transform: translateX(0);
    }
  }
}

.feature-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  border-radius: $radius-lg;
  margin-bottom: $spacing-md;
}

.feature-title {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0 0 $spacing-sm;
}

.feature-desc {
  font-size: $font-size-sm;
  color: $text-muted;
  line-height: $line-height-relaxed;
  margin: 0;
}

.feature-arrow {
  position: absolute;
  top: $spacing-lg;
  right: $spacing-lg;
  color: $primary-color;
  opacity: 0;
  transform: translateX(-8px);
  transition: all $transition-base;
}

.stats {
  padding: 60px 0;
  background: linear-gradient(135deg, $primary-color, $primary-light);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-lg;
  
  @include respond-to(md) {
    grid-template-columns: repeat(4, 1fr);
  }
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: $font-size-3xl;
  font-weight: $font-weight-bold;
  color: $text-white;
  margin-bottom: $spacing-xs;
}

.stat-label {
  font-size: $font-size-sm;
  color: rgba($text-white, 0.8);
}

.recent {
  padding: 80px 0;
  background: $bg-secondary;
}

.recent-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: $spacing-lg;
  margin-bottom: $spacing-xl;
  
  @include respond-to(md) {
    flex-direction: row;
    justify-content: space-between;
  }
  
  .section-title {
    margin: 0;
  }
}

.recent-tabs {
  display: flex;
  gap: $spacing-xs;
  background: $bg-tertiary;
  padding: $spacing-xs;
  border-radius: $radius-full;
}

.recent-tab {
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-muted;
  background: transparent;
  border: none;
  border-radius: $radius-full;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    color: $text-secondary;
  }
  
  &-active {
    color: $primary-color;
    background: $bg-primary;
  }
}

.recent-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.recent-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: $spacing-md $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    box-shadow: $shadow-md;
  }
}

.recent-item-main {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.recent-item-content {
  flex: 1;
}

.recent-item-title {
  font-size: $font-size-base;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
  
  @include truncate(1);
}

.recent-item-meta {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  font-size: $font-size-xs;
  color: $text-muted;
}

.recent-item-stats {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  font-size: $font-size-sm;
  color: $text-muted;
  
  span {
    display: flex;
    align-items: center;
    gap: $spacing-xs;
  }
}

.recent-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-md;
  
  @include respond-to(md) {
    grid-template-columns: repeat(4, 1fr);
  }
}

.product-card {
  background: $bg-primary;
  border-radius: $radius-lg;
  overflow: hidden;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    box-shadow: $shadow-md;
  }
}

.product-image {
  aspect-ratio: 1;
  background: $bg-tertiary;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.product-info {
  padding: $spacing-md;
}

.product-title {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
  
  @include truncate(1);
}

.product-price {
  font-size: $font-size-lg;
  font-weight: $font-weight-bold;
  color: $danger-color;
  margin-bottom: $spacing-xs;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  font-size: $font-size-xs;
  color: $text-muted;
}

.job-item {
  padding: $spacing-md $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    box-shadow: $shadow-md;
  }
}

.job-main {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.job-info {
  flex: 1;
}

.job-title {
  font-size: $font-size-base;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
}

.job-meta {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  font-size: $font-size-sm;
  color: $text-muted;
}

.job-salary {
  color: $danger-color;
  font-weight: $font-weight-semibold;
}

.cta {
  padding: 80px 0;
  background: linear-gradient(135deg, $bg-dark, #2A2A4E);
}

.cta-content {
  text-align: center;
}

.cta-title {
  font-size: $font-size-2xl;
  font-weight: $font-weight-bold;
  color: $text-white;
  margin: 0 0 $spacing-sm;
  
  @include respond-to(md) {
    font-size: $font-size-3xl;
  }
}

.cta-desc {
  font-size: $font-size-base;
  color: rgba($text-white, 0.7);
  margin: 0 0 $spacing-lg;
}
</style>
