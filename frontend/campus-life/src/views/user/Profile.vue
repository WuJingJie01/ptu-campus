<template>
  <MainLayout>
    <div class="profile-page">
      <div class="profile-container">
        <div class="profile-header">
          <div class="profile-cover"></div>
          <div class="profile-info">
            <div class="profile-avatar">
              <Avatar :src="user.avatar" :name="user.name" size="xl" />
              <Badge v-if="user.verified" type="primary" class="verified-badge">已认证</Badge>
            </div>
            <div class="profile-details">
              <h1 class="profile-name">{{ user.name }}</h1>
              <p v-if="user.college" class="profile-meta">{{ user.college }} · {{ user.grade }}</p>
              <p class="profile-bio">{{ user.bio }}</p>
            </div>
            <div class="profile-stats">
              <div class="stat-item" @click="showFollowers = true">
                <span class="stat-value">{{ user.followers }}</span>
                <span class="stat-label">粉丝</span>
              </div>
              <div class="stat-item" @click="showFollowing = true">
                <span class="stat-value">{{ user.following }}</span>
                <span class="stat-label">关注</span>
              </div>
            </div>
            <div class="profile-actions">
              <Button v-if="!isCurrentUser" type="primary" @click="handleFollow">
                {{ user.isFollowed ? '已关注' : '关注' }}
              </Button>
              <Button v-if="!isCurrentUser" type="outline" @click="router.push('/message')">
                私信
              </Button>
              <Button v-if="isCurrentUser" type="outline" @click="router.push('/user/settings')">
                编辑资料
              </Button>
            </div>
          </div>
        </div>

        <div class="profile-content">
          <Tabs :tabs="contentTabs" v-model="activeTab">
            <div v-if="activeTab === 'posts'" class="content-section">
              <div v-if="posts.length" class="posts-list">
                <div v-for="post in posts" :key="post.id" class="post-item" @click="router.push(`/forum/${post.id}`)">
                  <h4 class="post-title">{{ post.title }}</h4>
                  <p class="post-content">{{ post.content }}</p>
                  <div class="post-meta">
                    <span>{{ post.time }}</span>
                    <span>{{ post.likes }} 赞</span>
                    <span>{{ post.comments }} 评论</span>
                  </div>
                </div>
              </div>
              <Empty v-else title="暂无帖子" description="还没有发布过帖子" />
            </div>

            <div v-else-if="activeTab === 'products'" class="content-section">
              <div v-if="products.length" class="products-grid">
                <div v-for="product in products" :key="product.id" class="product-item" @click="router.push(`/market/${product.id}`)">
                  <img :src="product.image" :alt="product.title" />
                  <div class="product-info">
                    <h4>{{ product.title }}</h4>
                    <span class="product-price">¥{{ product.price }}</span>
                  </div>
                </div>
              </div>
              <Empty v-else title="暂无商品" description="还没有发布过商品" />
            </div>

            <div v-else-if="activeTab === 'jobs'" class="content-section">
              <div v-if="jobs.length" class="jobs-list">
                <div v-for="job in jobs" :key="job.id" class="job-item" @click="router.push(`/job/${job.id}`)">
                  <div class="job-info">
                    <h4>{{ job.title }}</h4>
                    <span class="job-salary">{{ job.salary }}</span>
                  </div>
                  <span class="job-time">{{ job.time }}</span>
                </div>
              </div>
              <Empty v-else title="暂无兼职" description="还没有发布过兼职" />
            </div>

            <div v-else-if="activeTab === 'reviews'" class="content-section">
              <div v-if="reviews.length" class="reviews-list">
                <div v-for="review in reviews" :key="review.id" class="review-item">
                  <div class="review-header">
                    <Avatar :src="review.user.avatar" :name="review.user.name" size="sm" />
                    <span class="review-user">{{ review.user.name }}</span>
                    <span class="review-time">{{ review.time }}</span>
                  </div>
                  <p class="review-content">{{ review.content }}</p>
                </div>
              </div>
              <Empty v-else title="暂无评价" description="还没有收到评价" />
            </div>
          </Tabs>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { MainLayout, Avatar, Badge, Button, Tabs, Empty } from '@/components'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeTab = ref('posts')
const showFollowers = ref(false)
const showFollowing = ref(false)

const userId = computed(() => route.params.id)
const isCurrentUser = computed(() => userId.value === String(userStore.user?.id))

const user = ref({
  id: 1,
  name: '张同学',
  avatar: '',
  verified: true,
  college: '计算机学院',
  grade: '2021级',
  bio: '热爱学习，热爱生活，喜欢分享',
  followers: 256,
  following: 128,
  isFollowed: false
})

const contentTabs = [
  { label: '帖子', value: 'posts' },
  { label: '商品', value: 'products' },
  { label: '兼职', value: 'jobs' },
  { label: '评价', value: 'reviews' }
]

const posts = ref([
  {
    id: 1,
    title: '关于图书馆自习室预约系统的使用心得分享',
    content: '最近发现图书馆的预约系统真的很好用，分享一下我的使用心得...',
    time: '2024-03-15',
    likes: 128,
    comments: 32
  },
  {
    id: 2,
    title: '考研经验分享：如何高效备考数学',
    content: '作为一名已经上岸的学长，想和大家分享一下考研数学的备考经验...',
    time: '2024-03-10',
    likes: 389,
    comments: 67
  }
])

const products = ref([
  { id: 1, title: 'iPad Pro 11寸', price: 4500, image: 'https://picsum.photos/200/200?random=50' },
  { id: 2, title: '高等数学教材', price: 25, image: 'https://picsum.photos/200/200?random=51' }
])

const jobs = ref([
  { id: 1, title: '家教辅导（数学）', salary: '50元/小时', time: '2024-03-12' }
])

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

const handleFollow = () => {
  user.value.isFollowed = !user.value.isFollowed
  user.value.followers += user.value.isFollowed ? 1 : -1
}
</script>

<style lang="scss" scoped>
.profile-page {
  min-height: 100vh;
  background: $bg-secondary;
}

.profile-container {
  max-width: 900px;
  margin: 0 auto;
}

.profile-header {
  position: relative;
  background: $bg-primary;
  margin-bottom: $spacing-lg;
}

.profile-cover {
  height: 200px;
  background: linear-gradient(135deg, $primary-color, $primary-light);
}

.profile-info {
  position: relative;
  padding: 0 $spacing-lg $spacing-lg;
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: $spacing-lg;
  margin-top: -50px;
}

.profile-avatar {
  position: relative;
}

.verified-badge {
  position: absolute;
  bottom: 0;
  right: 0;
}

.profile-details {
  flex: 1;
  min-width: 200px;
  padding-top: 60px;
}

.profile-name {
  font-size: $font-size-2xl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
}

.profile-meta {
  font-size: $font-size-sm;
  color: $text-muted;
  margin: 0 0 $spacing-sm;
}

.profile-bio {
  font-size: $font-size-sm;
  color: $text-secondary;
  margin: 0;
}

.profile-stats {
  display: flex;
  gap: $spacing-xl;
  padding-top: 60px;
}

.stat-item {
  text-align: center;
  cursor: pointer;
  
  &:hover .stat-value {
    color: $primary-color;
  }
}

.stat-value {
  display: block;
  font-size: $font-size-xl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  transition: color $transition-fast;
}

.stat-label {
  font-size: $font-size-sm;
  color: $text-muted;
}

.profile-actions {
  display: flex;
  gap: $spacing-md;
  padding-top: 60px;
}

.profile-content {
  padding: 0 $spacing-lg $spacing-lg;
}

.content-section {
  min-height: 300px;
}

.posts-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.post-item {
  padding: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    box-shadow: $shadow-md;
  }
}

.post-title {
  font-size: $font-size-base;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
}

.post-content {
  font-size: $font-size-sm;
  color: $text-secondary;
  margin: 0 0 $spacing-sm;
  
  @include truncate(2);
}

.post-meta {
  display: flex;
  gap: $spacing-md;
  font-size: $font-size-xs;
  color: $text-muted;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: $spacing-md;
  
  @include respond-to(sm) {
    grid-template-columns: repeat(3, 1fr);
  }
}

.product-item {
  background: $bg-primary;
  border-radius: $radius-lg;
  overflow: hidden;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    box-shadow: $shadow-md;
  }
  
  img {
    width: 100%;
    aspect-ratio: 1;
    object-fit: cover;
  }
}

.product-info {
  padding: $spacing-md;
  
  h4 {
    font-size: $font-size-sm;
    color: $text-primary;
    margin: 0 0 $spacing-xs;
    
    @include truncate(1);
  }
}

.product-price {
  font-size: $font-size-base;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

.jobs-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.job-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-md $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    box-shadow: $shadow-md;
  }
}

.job-info h4 {
  font-size: $font-size-base;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
}

.job-salary {
  font-size: $font-size-sm;
  font-weight: $font-weight-bold;
  color: $danger-color;
}

.job-time {
  font-size: $font-size-sm;
  color: $text-muted;
}

.reviews-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.review-item {
  padding: $spacing-md $spacing-lg;
  background: $bg-primary;
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
</style>
