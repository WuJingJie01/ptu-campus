<template>
  <MainLayout>
    <div class="detail-page">
      <div class="detail-container">
        <article class="detail-main">
          <div class="post-detail">
            <header class="post-header">
              <h1 class="post-title">{{ post.title }}</h1>
              <div class="post-meta">
                <div class="post-author" @click="router.push(`/user/${post.author.id}`)">
                  <Avatar :src="post.author.avatar" :name="post.author.name" size="md" clickable />
                  <div class="post-author-info">
                    <div class="post-author-name">
                      {{ post.author.name }}
                      <Badge v-if="post.author.verified" type="primary" size="sm">已认证</Badge>
                    </div>
                    <div class="post-author-extra">
                      <span>{{ post.author.college }}</span>
                      <span>·</span>
                      <span>{{ post.author.grade }}</span>
                    </div>
                  </div>
                </div>
                <div class="post-time">{{ post.time }}</div>
              </div>
            </header>

            <div class="post-content">
              <p v-for="(para, index) in post.paragraphs" :key="index">{{ para }}</p>
            </div>

            <div class="post-tags">
              <span v-for="tag in post.tags" :key="tag" class="post-tag">#{{ tag }}</span>
            </div>

            <footer class="post-footer">
              <div class="post-actions">
                <button class="action-btn" :class="{ 'action-btn-active': post.liked }" @click="handleLike">
                  <svg width="20" height="20" viewBox="0 0 24 24" :fill="post.liked ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                    <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" />
                  </svg>
                  <span>{{ post.likes }} 赞</span>
                </button>
                <button class="action-btn" :class="{ 'action-btn-active': post.collected }" @click="handleCollect">
                  <svg width="20" height="20" viewBox="0 0 24 24" :fill="post.collected ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                    <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z" />
                  </svg>
                  <span>{{ post.collected ? '已收藏' : '收藏' }}</span>
                </button>
                <button class="action-btn" @click="handleShare">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="18" cy="5" r="3" />
                    <circle cx="6" cy="12" r="3" />
                    <circle cx="18" cy="19" r="3" />
                    <line x1="8.59" y1="13.51" x2="15.42" y2="17.49" />
                    <line x1="15.41" y1="6.51" x2="8.59" y2="10.49" />
                  </svg>
                  <span>分享</span>
                </button>
                <button class="action-btn" @click="showReportModal = true">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z" />
                    <line x1="4" y1="22" x2="4" y2="15" />
                  </svg>
                  <span>举报</span>
                </button>
              </div>
            </footer>
          </div>

          <section class="comments-section">
            <h3 class="comments-title">评论 ({{ comments.length }})</h3>

            <div class="comment-input">
              <Avatar :src="userStore.user?.avatar" :name="userStore.user?.nickname" size="md" />
              <div class="comment-input-wrapper">
                <textarea
                  v-model="commentText"
                  placeholder="写下你的评论..."
                  rows="3"
                  class="comment-textarea"
                ></textarea>
                <div class="comment-input-actions">
                  <button class="at-btn" @click="showAtList = !showAtList">
                    @ 提及用户
                  </button>
                  <Button type="primary" size="sm" :disabled="!commentText.trim()" @click="submitComment">
                    发表评论
                  </Button>
                </div>
              </div>
            </div>

            <div class="comments-list">
              <div v-for="comment in comments" :key="comment.id" class="comment-item">
                <Avatar :src="comment.author.avatar" :name="comment.author.name" size="md" clickable />
                <div class="comment-content">
                  <div class="comment-header">
                    <span class="comment-author">{{ comment.author.name }}</span>
                    <span v-if="comment.replyTo" class="comment-reply-to">
                      回复 <span class="comment-reply-name">@{{ comment.replyTo }}</span>
                    </span>
                    <span class="comment-time">{{ comment.time }}</span>
                  </div>
                  <p class="comment-text">{{ comment.content }}</p>
                  <div class="comment-actions">
                    <button class="comment-action-btn" @click="handleCommentLike(comment)">
                      <svg width="16" height="16" viewBox="0 0 24 24" :fill="comment.liked ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                        <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" />
                      </svg>
                      {{ comment.likes }}
                    </button>
                    <button class="comment-action-btn" @click="handleReplyClick(comment.id)">
                      {{ activeReplyId === comment.id ? '取消回复' : '回复' }}
                    </button>
                  </div>

                  <div v-if="activeReplyId === comment.id" class="reply-input-wrapper">
                    <div class="reply-input-container">
                      <input
                        v-model="replyContent"
                        :placeholder="`回复 @${comment.author.name}...`"
                        class="reply-input"
                        @keyup.enter="submitReply(comment.id, comment.author.name)"
                      />
                      <Button type="primary" size="sm" :disabled="!replyContent.trim()" @click="submitReply(comment.id, comment.author.name)">
                        发送
                      </Button>
                    </div>
                  </div>

                  <div v-if="comment.replies?.length" class="comment-replies">
                    <div v-for="reply in comment.replies" :key="reply.id" class="comment-reply-item">
                      <Avatar :src="reply.author.avatar" :name="reply.author.name" size="sm" />
                      <div class="comment-reply-content">
                        <span class="comment-reply-author">{{ reply.author.name }}</span>
                        <span v-if="reply.replyTo" class="comment-reply-to-text">
                          回复 <span class="comment-reply-name">@{{ reply.replyTo }}</span>
                        </span>
                        <span class="comment-reply-text">：{{ reply.content }}</span>
                        <span class="comment-reply-time">{{ reply.time }}</span>
                        <button class="comment-action-btn" @click="handleReplyClick(reply.id)">
                          {{ activeReplyId === reply.id ? '取消回复' : '回复' }}
                        </button>
                      </div>

                      <div v-if="activeReplyId === reply.id" class="reply-input-wrapper mt-sm">
                        <div class="reply-input-container">
                          <input
                            v-model="replyContent"
                            :placeholder="`回复 @${reply.author.name}...`"
                            class="reply-input"
                            @keyup.enter="submitReply(comment.id, reply.author.name)"
                          />
                          <Button type="primary" size="sm" :disabled="!replyContent.trim()" @click="submitReply(comment.id, reply.author.name)">
                            发送
                          </Button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>
        </article>

        <aside class="detail-sidebar">
          <Card title="作者信息" class="sidebar-card">
            <div class="author-card">
              <Avatar :src="post.author.avatar" :name="post.author.name" size="xl" />
              <h4 class="author-name">{{ post.author.name }}</h4>
              <p class="author-bio">{{ post.author.bio }}</p>
              <div class="author-stats">
                <div class="author-stat">
                  <span class="author-stat-value">{{ post.author.followers }}</span>
                  <span class="author-stat-label">粉丝</span>
                </div>
                <div class="author-stat">
                  <span class="author-stat-value">{{ post.author.following }}</span>
                  <span class="author-stat-label">关注</span>
                </div>
              </div>
              <Button type="primary" block @click="handleFollow">
                {{ post.author.isFollowed ? '已关注' : '关注' }}
              </Button>
            </div>
          </Card>

          <Card title="相关推荐" class="sidebar-card">
            <div class="related-list">
              <div
                v-for="item in relatedPosts"
                :key="item.id"
                class="related-item"
                @click="router.push(`/forum/${item.id}`)"
              >
                <h5 class="related-title">{{ item.title }}</h5>
                <div class="related-meta">
                  <span>{{ item.likes }} 赞</span>
                  <span>·</span>
                  <span>{{ item.comments }} 评论</span>
                </div>
              </div>
            </div>
          </Card>
        </aside>
      </div>
    </div>

    <Modal v-model="showReportModal" title="举报内容" size="sm">
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
import { MainLayout, Avatar, Badge, Card, Button, Modal } from '@/components'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const commentText = ref('')
const showAtList = ref(false)
const showReportModal = ref(false)
const reportType = ref('')
const reportReason = ref('')

const reportOptions = ['广告', '色情', '暴力', '虚假信息', '其他']

const post = ref({
  id: 1,
  title: '关于图书馆自习室预约系统的使用心得分享',
  author: {
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
  },
  time: '2024-03-15 14:30',
  paragraphs: [
    '最近发现图书馆的预约系统真的很好用，分享一下我的使用心得。',
    '首先，建议大家提前一天预约，这样成功率会更高。预约系统每天晚上12点开放第二天的预约名额，热门时段（如考试周）建议准时抢。',
    '其次，预约成功后一定要按时签到。如果连续三次预约后未签到，会被系统拉入黑名单一周，无法再进行预约。',
    '最后，如果临时有事去不了，记得提前取消预约，把名额让给其他同学。取消预约需要在预约时间前30分钟完成。',
    '希望这些经验对大家有帮助，祝大家都能找到满意的学习位置！'
  ],
  tags: ['图书馆', '自习', '学习经验'],
  likes: 128,
  liked: false,
  collected: false
})

const comments = ref([
  {
    id: 1,
    author: { name: '李同学', avatar: '' },
    content: '太有用了！之前一直不知道怎么预约，现在终于学会了',
    time: '10分钟前',
    likes: 23,
    liked: false,
    replies: [
      {
        id: 11,
        author: { name: '张同学', avatar: '' },
        content: '有帮助就好！',
        time: '5分钟前',
        replyTo: '李同学'
      }
    ]
  },
  {
    id: 2,
    author: { name: '王同学', avatar: '' },
    content: '请问一下，如果预约了但忘记签到会怎么样？',
    time: '30分钟前',
    likes: 5,
    liked: false,
    replies: []
  }
])

const relatedPosts = [
  { id: 2, title: '考研经验分享：如何高效备考数学', likes: 389, comments: 67 },
  { id: 3, title: '食堂新推出的菜品测评，强烈推荐！', likes: 256, comments: 48 },
  { id: 4, title: '2024届毕业生就业情况统计', likes: 198, comments: 35 }
]

const handleLike = () => {
  post.value.liked = !post.value.liked
  post.value.likes += post.value.liked ? 1 : -1
}

const handleCollect = () => {
  post.value.collected = !post.value.collected
}

const handleShare = () => {
  navigator.clipboard.writeText(window.location.href)
  alert('链接已复制到剪贴板')
}

const handleFollow = () => {
  post.value.author.isFollowed = !post.value.author.isFollowed
}

const handleCommentLike = (comment: any) => {
  comment.liked = !comment.liked
  comment.likes += comment.liked ? 1 : -1
}

const replyTo = (comment: any) => {
  commentText.value = `@${comment.author.name} `
}

const activeReplyId = ref<number | null>(null)
const replyContent = ref('')

const handleReplyClick = (id: number) => {
  if (activeReplyId.value === id) {
    activeReplyId.value = null
    replyContent.value = ''
  } else {
    activeReplyId.value = id
    replyContent.value = ''
  }
}

const submitReply = (commentId: number, replyToUser?: string) => {
  if (!replyContent.value.trim()) return

  const targetComment = comments.value.find(c => c.id === commentId)
  if (targetComment) {
    if (!targetComment.replies) {
      targetComment.replies = []
    }
    targetComment.replies.push({
      id: Date.now(),
      author: {
        name: userStore.user?.nickname || '我',
        avatar: userStore.user?.avatar || ''
      },
      content: replyContent.value,
      time: '刚刚',
      replyTo: replyToUser,
      likes: 0
    })
  }

  activeReplyId.value = null
  replyContent.value = ''
}

const submitComment = () => {
  if (!commentText.value.trim()) return
  comments.value.unshift({
    id: Date.now(),
    author: { name: userStore.user?.nickname || '匿名用户', avatar: userStore.user?.avatar || '' },
    content: commentText.value,
    time: '刚刚',
    likes: 0,
    liked: false,
    replies: []
  })
  commentText.value = ''
}

const submitReport = () => {
  alert('举报已提交，等待审核')
  showReportModal.value = false
  reportType.value = ''
  reportReason.value = ''
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

.post-detail {
  background: $bg-primary;
  border-radius: $radius-xl;
  padding: $spacing-xl;
}

.post-header {
  margin-bottom: $spacing-xl;
}

.post-title {
  font-size: $font-size-2xl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  line-height: 1.4;
  margin: 0 0 $spacing-lg;
}

.post-meta {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
  
  @include respond-to(sm) {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
}

.post-author {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  cursor: pointer;
}

.post-author-info {
  flex: 1;
}

.post-author-name {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  font-size: $font-size-base;
  font-weight: $font-weight-medium;
  color: $text-primary;
}

.post-author-extra {
  font-size: $font-size-sm;
  color: $text-muted;
  margin-top: 2px;
}

.post-time {
  font-size: $font-size-sm;
  color: $text-muted;
}

.post-content {
  margin-bottom: $spacing-xl;
  
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

.post-tags {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-sm;
  margin-bottom: $spacing-xl;
}

.post-tag {
  padding: $spacing-xs $spacing-md;
  font-size: $font-size-sm;
  color: $primary-color;
  background: $primary-bg;
  border-radius: $radius-full;
}

.post-footer {
  padding-top: $spacing-lg;
  border-top: 1px solid $border-color;
}

.post-actions {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-md;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-sm $spacing-md;
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
    color: $primary-color;
    background: $primary-bg;
  }
}

.comments-section {
  margin-top: $spacing-xl;
  background: $bg-primary;
  border-radius: $radius-xl;
  padding: $spacing-xl;
}

.comments-title {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0 0 $spacing-lg;
}

.comment-input {
  display: flex;
  gap: $spacing-md;
  margin-bottom: $spacing-xl;
  padding-bottom: $spacing-xl;
  border-bottom: 1px solid $border-color;
}

.comment-input-wrapper {
  flex: 1;
}

.comment-textarea {
  width: 100%;
  padding: $spacing-md;
  font-size: $font-size-sm;
  background: $bg-tertiary;
  border: 1px solid transparent;
  border-radius: $radius-lg;
  resize: none;
  outline: none;
  transition: all $transition-fast;
  
  &:focus {
    background: $bg-primary;
    border-color: $primary-color;
  }
  
  &::placeholder {
    color: $text-muted;
  }
}

.comment-input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: $spacing-sm;
}

.at-btn {
  padding: $spacing-xs $spacing-sm;
  font-size: $font-size-sm;
  color: $text-muted;
  background: transparent;
  border: none;
  cursor: pointer;
  
  &:hover {
    color: $primary-color;
  }
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.comment-item {
  display: flex;
  gap: $spacing-md;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  margin-bottom: $spacing-xs;
}

.comment-author {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
}

.comment-reply-to {
  font-size: $font-size-sm;
  color: $text-muted;
}

.comment-reply-name {
  color: $primary-color;
}

.comment-time {
  font-size: $font-size-xs;
  color: $text-light;
}

.comment-text {
  font-size: $font-size-sm;
  color: $text-secondary;
  line-height: $line-height-relaxed;
  margin: 0 0 $spacing-sm;
}

.comment-actions {
  display: flex;
  gap: $spacing-md;
}

.comment-action-btn {
  font-size: $font-size-xs;
  color: $text-muted;
  background: transparent;
  border: none;
  cursor: pointer;
  
  &:hover {
    color: $primary-color;
  }
}

.comment-replies {
  margin-top: $spacing-md;
  padding: $spacing-md;
  background: $bg-tertiary;
  border-radius: $radius-lg;
}

.comment-reply-item {
  display: flex;
  gap: $spacing-sm;
  margin-bottom: $spacing-sm;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.comment-reply-content {
  flex: 1;
  font-size: $font-size-sm;
}

.comment-reply-author {
  font-weight: $font-weight-medium;
  color: $text-primary;
}

.comment-reply-to-text {
  color: $text-muted;
}

.comment-reply-text {
  color: $text-secondary;
}

.comment-reply-time {
  color: $text-light;
  margin-left: $spacing-sm;
}

.detail-sidebar {
  display: flex;
  flex-direction: column;
  gap: $spacing-lg;
}

.sidebar-card {
  margin-bottom: 0;
}

.author-card {
  text-align: center;
}

.author-name {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: $spacing-md 0 $spacing-sm;
}

.author-bio {
  font-size: $font-size-sm;
  color: $text-muted;
  margin: 0 0 $spacing-md;
}

.author-stats {
  display: flex;
  justify-content: center;
  gap: $spacing-xl;
  margin-bottom: $spacing-md;
}

.author-stat {
  text-align: center;
}

.author-stat-value {
  display: block;
  font-size: $font-size-lg;
  font-weight: $font-weight-bold;
  color: $text-primary;
}

.author-stat-label {
  font-size: $font-size-xs;
  color: $text-muted;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.related-item {
  padding: $spacing-sm;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
}

.related-title {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin: 0 0 $spacing-xs;
  
  @include truncate(2);
}

.related-meta {
  font-size: $font-size-xs;
  color: $text-muted;
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

.reply-input-wrapper {
  margin-top: $spacing-sm;
  padding: $spacing-sm;
  background: $bg-tertiary;
  border-radius: $radius-md;
}

.reply-input-container {
  display: flex;
  gap: $spacing-sm;
}

.reply-input {
  flex: 1;
  padding: $spacing-xs $spacing-sm;
  font-size: $font-size-sm;
  border: 1px solid $border-color;
  border-radius: $radius-md;
  outline: none;
  background: $bg-primary;
  
  &:focus {
    border-color: $primary-color;
  }
}
</style>
