<template>
  <MainLayout>
    <div class="forum-page">
      <div class="forum-container">
        <div class="forum-main">
          <div class="forum-header">
            <div class="forum-categories">
              <button
                v-for="category in categories"
                :key="category.value"
                :class="['category-btn', { 'category-btn-active': activeCategory === category.value }]"
                @click="activeCategory = category.value"
              >
                {{ category.label }}
              </button>
            </div>
            <Button type="primary" @click="router.push('/forum/post')">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              发布帖子
            </Button>
          </div>

          <div class="forum-list">
            <div
              v-for="post in posts"
              :key="post.id"
              class="post-card"
              @click="router.push(`/forum/${post.id}`)"
            >
              <div class="post-header">
                <Avatar :src="post.author.avatar" :name="post.author.name" size="md" clickable />
                <div class="post-author-info">
                  <div class="post-author-name">
                    {{ post.author.name }}
                    <Badge v-if="post.author.verified" type="primary" size="sm">已认证</Badge>
                  </div>
                  <div class="post-meta">
                    <span>{{ post.time }}</span>
                    <span>·</span>
                    <span>{{ post.category }}</span>
                  </div>
                </div>
              </div>
              
              <h3 class="post-title">{{ post.title }}</h3>
              <p class="post-content">{{ post.content }}</p>
              
              <div class="post-footer">
                <div class="post-stats">
                  <button class="stat-btn" @click.stop="handleLike(post)">
                    <svg
                      width="18"
                      height="18"
                      viewBox="0 0 24 24"
                      :fill="post.liked ? 'currentColor' : 'none'"
                      stroke="currentColor"
                      stroke-width="2"
                      :class="{ 'stat-btn-active': post.liked }"
                    >
                      <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" />
                    </svg>
                    <span>{{ post.likes }}</span>
                  </button>
                  <button class="stat-btn">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
                    </svg>
                    <span>{{ post.comments }}</span>
                  </button>
                  <button class="stat-btn" @click.stop="handleCollect(post)">
                    <svg
                      width="18"
                      height="18"
                      viewBox="0 0 24 24"
                      :fill="post.collected ? 'currentColor' : 'none'"
                      stroke="currentColor"
                      stroke-width="2"
                      :class="{ 'stat-btn-active': post.collected }"
                    >
                      <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z" />
                    </svg>
                  </button>
                </div>
                <button class="stat-btn" @click.stop="handleShare(post)">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="18" cy="5" r="3" />
                    <circle cx="6" cy="12" r="3" />
                    <circle cx="18" cy="19" r="3" />
                    <line x1="8.59" y1="13.51" x2="15.42" y2="17.49" />
                    <line x1="15.41" y1="6.51" x2="8.59" y2="10.49" />
                  </svg>
                  <span>分享</span>
                </button>
              </div>
            </div>
          </div>

          <div class="forum-pagination">
            <button class="page-btn" :disabled="currentPage === 1" @click="currentPage--">
              上一页
            </button>
            <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
            <button class="page-btn" :disabled="currentPage === totalPages" @click="currentPage++">
              下一页
            </button>
          </div>
        </div>

        <aside class="forum-sidebar">
          <Card title="🔥 热门帖子" class="sidebar-card">
            <div class="hot-list">
              <div
                v-for="(item, index) in hotPosts"
                :key="item.id"
                class="hot-item"
                @click="router.push(`/forum/${item.id}`)"
              >
                <span class="hot-rank" :class="{ 'hot-rank-top': index < 3 }">{{ index + 1 }}</span>
                <span class="hot-title">{{ item.title }}</span>
              </div>
            </div>
          </Card>

          <Card title="⭐ 热门创作者" class="sidebar-card">
            <div class="creator-list">
              <div
                v-for="creator in hotCreators"
                :key="creator.id"
                class="creator-item"
                @click="router.push(`/user/${creator.id}`)"
              >
                <Avatar :src="creator.avatar" :name="creator.name" size="md" clickable />
                <div class="creator-info">
                  <div class="creator-name">{{ creator.name }}</div>
                  <div class="creator-stats">{{ creator.posts }} 篇帖子</div>
                </div>
              </div>
            </div>
          </Card>

          <Card title="💬 热议话题" class="sidebar-card">
            <div class="topic-list">
              <div v-for="topic in hotTopics" :key="topic" class="topic-item">
                <span class="topic-hash">#</span>
                <span class="topic-text">{{ topic }}</span>
              </div>
            </div>
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
const currentPage = ref(1)
const totalPages = ref(10)

const categories = [
  { label: '全部', value: 'all' },
  { label: '校园生活', value: 'life' },
  { label: '学习交流', value: 'study' },
  { label: '失物招领', value: 'lost' },
  { label: '二手交易', value: 'trade' },
  { label: '求职招聘', value: 'job' }
]

const posts = ref([
  {
    id: 1,
    title: '关于图书馆自习室预约系统的使用心得分享',
    content: '最近发现图书馆的预约系统真的很好用，分享一下我的使用心得。首先，建议大家提前一天预约，这样成功率会更高。其次，预约成功后一定要按时签到...',
    author: { name: '张同学', avatar: '', verified: true },
    time: '10分钟前',
    category: '学习交流',
    likes: 128,
    comments: 32,
    liked: false,
    collected: false
  },
  {
    id: 2,
    title: '食堂新推出的菜品测评，强烈推荐！',
    content: '今天去食堂吃饭，发现窗口新推出了几道菜，尝了一下味道真的很不错！特别是那个糖醋里脊，外酥里嫩，酸甜适中，强烈推荐大家去尝尝...',
    author: { name: '李同学', avatar: '', verified: false },
    time: '30分钟前',
    category: '校园生活',
    likes: 256,
    comments: 48,
    liked: true,
    collected: false
  },
  {
    id: 3,
    title: '寻找丢失的校园卡，学号为2021XXXX',
    content: '今天下午在图书馆三楼自习室丢失了一张校园卡，学号是2021XXXX，姓名是王XX。如果有同学捡到，请联系我，万分感谢！',
    author: { name: '王同学', avatar: '', verified: false },
    time: '1小时前',
    category: '失物招领',
    likes: 45,
    comments: 12,
    liked: false,
    collected: true
  },
  {
    id: 4,
    title: '考研经验分享：如何高效备考数学',
    content: '作为一名已经上岸的学长，想和大家分享一下考研数学的备考经验。首先，基础很重要，一定要把课本上的知识点都弄懂...',
    author: { name: '赵同学', avatar: '', verified: true },
    time: '2小时前',
    category: '学习交流',
    likes: 389,
    comments: 67,
    liked: false,
    collected: false
  },
  {
    id: 5,
    title: '周末约球！羽毛球爱好者集合',
    content: '这周末想约几个同学一起打羽毛球，有没有感兴趣的同学？时间暂定周六下午3点，地点在体育馆羽毛球场，有意向的留言或者私信我~',
    author: { name: '周同学', avatar: '', verified: false },
    time: '3小时前',
    category: '校园生活',
    likes: 78,
    comments: 23,
    liked: false,
    collected: false
  }
])

const hotPosts = [
  { id: 1, title: '考研经验分享：如何高效备考数学' },
  { id: 2, title: '食堂新推出的菜品测评，强烈推荐！' },
  { id: 3, title: '关于图书馆自习室预约系统的使用心得' },
  { id: 4, title: '2024届毕业生就业情况统计' },
  { id: 5, title: '校内电动车管理规定更新通知' }
]

const hotCreators = [
  { id: 1, name: '张同学', avatar: '', posts: 156 },
  { id: 2, name: '李同学', avatar: '', posts: 98 },
  { id: 3, name: '王同学', avatar: '', posts: 87 },
  { id: 4, name: '赵同学', avatar: '', posts: 65 }
]

const hotTopics = [
  '考研备考',
  '校园美食',
  '毕业季',
  '实习求职',
  '社团活动'
]

const handleLike = (post: any) => {
  post.liked = !post.liked
  post.likes += post.liked ? 1 : -1
}

const handleCollect = (post: any) => {
  post.collected = !post.collected
}

const handleShare = (post: any) => {
  navigator.clipboard.writeText(`${window.location.origin}/forum/${post.id}`)
  alert('链接已复制到剪贴板')
}
</script>

<style lang="scss" scoped>
.forum-page {
  min-height: 100vh;
  padding: $spacing-lg 0;
}

.forum-container {
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

.forum-header {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
  
  @include respond-to(md) {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
}

.forum-categories {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-xs;
}

.category-btn {
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
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
    
    &:hover {
      color: $text-white;
      background: $primary-dark;
    }
  }
}

.forum-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.post-card {
  padding: $spacing-lg;
  background: $bg-primary;
  border-radius: $radius-lg;
  border: 1px solid $border-color;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    border-color: $primary-color;
    box-shadow: $shadow-md;
  }
}

.post-header {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  margin-bottom: $spacing-md;
}

.post-author-info {
  flex: 1;
}

.post-author-name {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  font-size: $font-size-xs;
  color: $text-muted;
  margin-top: 2px;
}

.post-title {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0 0 $spacing-sm;
  
  @include truncate(2);
}

.post-content {
  font-size: $font-size-sm;
  color: $text-secondary;
  line-height: $line-height-relaxed;
  margin: 0 0 $spacing-md;
  
  @include truncate(3);
}

.post-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: $spacing-md;
  border-top: 1px solid $border-light;
}

.post-stats {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.stat-btn {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-xs $spacing-sm;
  font-size: $font-size-sm;
  color: $text-muted;
  background: transparent;
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

.forum-pagination {
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

.forum-sidebar {
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
  gap: $spacing-sm;
}

.hot-item {
  display: flex;
  align-items: flex-start;
  gap: $spacing-sm;
  padding: $spacing-sm;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
}

.hot-rank {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  font-size: $font-size-xs;
  font-weight: $font-weight-bold;
  color: $text-muted;
  background: $bg-tertiary;
  border-radius: $radius-sm;
  flex-shrink: 0;
  
  &-top {
    color: $text-white;
    background: $primary-color;
  }
}

.hot-title {
  font-size: $font-size-sm;
  color: $text-secondary;
  line-height: 1.4;
  
  @include truncate(2);
}

.creator-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.creator-item {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-sm;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
}

.creator-info {
  flex: 1;
}

.creator-name {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
}

.creator-stats {
  font-size: $font-size-xs;
  color: $text-muted;
  margin-top: 2px;
}

.topic-list {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-sm;
}

.topic-item {
  display: flex;
  align-items: center;
  padding: $spacing-xs $spacing-sm;
  background: $bg-tertiary;
  border-radius: $radius-full;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    background: $primary-bg;
    
    .topic-text {
      color: $primary-color;
    }
  }
}

.topic-hash {
  color: $primary-color;
  font-weight: $font-weight-bold;
  margin-right: 2px;
}

.topic-text {
  font-size: $font-size-xs;
  color: $text-secondary;
}
</style>
