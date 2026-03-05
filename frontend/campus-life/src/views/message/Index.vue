<template>
  <MainLayout>
    <div class="message-page">
      <div class="message-container">
        <aside class="message-sidebar">
          <div class="sidebar-header">
            <h2>消息中心</h2>
          </div>
          <nav class="message-nav">
            <button
              v-for="item in navItems"
              :key="item.value"
              :class="['nav-item', { 'nav-item-active': activeNav === item.value }]"
              @click="activeNav = item.value"
            >
              <span class="nav-icon" v-html="item.icon"></span>
              <span class="nav-label">{{ item.label }}</span>
              <span v-if="item.unread" class="nav-badge">{{ item.unread }}</span>
            </button>
          </nav>
        </aside>

        <main class="message-main">
          <div v-if="activeNav === 'system'" class="message-list">
            <div class="list-header">
              <h3>系统通知</h3>
            </div>
            <div class="list-content">
              <div
                v-for="item in systemMessages"
                :key="item.id"
                :class="['message-item', { 'message-item-unread': !item.read }]"
                @click="item.read = true"
              >
                <div class="message-icon system">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
                    <path d="M13.73 21a2 2 0 0 1-3.46 0" />
                  </svg>
                </div>
                <div class="message-content">
                  <h4>{{ item.title }}</h4>
                  <p>{{ item.content }}</p>
                  <span class="message-time">{{ item.time }}</span>
                </div>
              </div>
            </div>
          </div>

          <div v-else-if="activeNav === 'comments'" class="message-list">
            <div class="list-header">
              <h3>评论与@</h3>
            </div>
            <div class="list-content">
              <div
                v-for="item in commentMessages"
                :key="item.id"
                :class="['message-item', { 'message-item-unread': !item.read }]"
                @click="item.read = true"
              >
                <Avatar :src="item.user.avatar" :name="item.user.name" size="md" />
                <div class="message-content">
                  <p><strong>{{ item.user.name }}</strong> {{ item.action }}</p>
                  <p class="message-quote">{{ item.quote }}</p>
                  <span class="message-time">{{ item.time }}</span>
                </div>
              </div>
            </div>
          </div>

          <div v-else-if="activeNav === 'likes'" class="message-list">
            <div class="list-header">
              <h3>赞和收藏</h3>
            </div>
            <div class="list-content">
              <div
                v-for="item in likeMessages"
                :key="item.id"
                :class="['message-item', { 'message-item-unread': !item.read }]"
                @click="item.read = true"
              >
                <Avatar :src="item.user.avatar" :name="item.user.name" size="md" />
                <div class="message-content">
                  <p><strong>{{ item.user.name }}</strong> {{ item.action }}了你的{{ item.type }}</p>
                  <p class="message-quote">{{ item.title }}</p>
                  <span class="message-time">{{ item.time }}</span>
                </div>
              </div>
            </div>
          </div>

          <div v-else-if="activeNav === 'private'" class="chat-view">
            <div class="chat-list">
              <div class="list-header">
                <h3>私信</h3>
              </div>
              <div class="chat-users">
                <div
                  v-for="chat in chatList"
                  :key="chat.id"
                  :class="['chat-user', { 'chat-user-active': activeChat === chat.id }]"
                  @click="activeChat = chat.id"
                >
                  <Avatar :src="chat.user.avatar" :name="chat.user.name" size="md" />
                  <div class="chat-info">
                    <span class="chat-name">{{ chat.user.name }}</span>
                    <span class="chat-preview">{{ chat.lastMessage }}</span>
                  </div>
                  <span v-if="chat.unread" class="chat-badge"></span>
                </div>
              </div>
            </div>
            <div class="chat-content">
              <div v-if="activeChat" class="chat-messages">
                <div class="chat-header">
                  <Avatar :src="currentChat?.user.avatar" :name="currentChat?.user.name" size="sm" />
                  <span>{{ currentChat?.user.name }}</span>
                </div>
                <div class="messages-list" ref="messagesListRef">
                  <div
                    v-for="msg in currentChat?.messages"
                    :key="msg.id"
                    :class="['chat-message', `chat-message-${msg.role}`]"
                  >
                    <p>{{ msg.content }}</p>
                    <span class="msg-time">{{ msg.time }}</span>
                  </div>
                </div>
                <div class="chat-input">
                  <input
                    v-model="chatInput"
                    type="text"
                    placeholder="输入消息..."
                    @keyup.enter="sendChatMessage"
                  />
                  <Button type="primary" size="sm" @click="sendChatMessage">发送</Button>
                </div>
              </div>
              <Empty v-else title="选择对话" description="从左侧选择一个对话开始聊天" />
            </div>
          </div>

          <div v-else-if="activeNav === 'follow'" class="message-list">
            <div class="list-header">
              <h3>关注动态</h3>
            </div>
            <div class="list-content">
              <div
                v-for="item in followMessages"
                :key="item.id"
                :class="['message-item', { 'message-item-unread': !item.read }]"
                @click="item.read = true"
              >
                <Avatar :src="item.user.avatar" :name="item.user.name" size="md" />
                <div class="message-content">
                  <p><strong>{{ item.user.name }}</strong> 发布了新的{{ item.type }}</p>
                  <p class="message-quote">{{ item.title }}</p>
                  <span class="message-time">{{ item.time }}</span>
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
import { MainLayout, Avatar, Button, Empty } from '@/components'

const activeNav = ref('system')
const activeChat = ref<number | null>(null)
const chatInput = ref('')
const messagesListRef = ref<HTMLElement | null>(null)

const navItems = [
  { label: '系统通知', value: 'system', icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" /><path d="M13.73 21a2 2 0 0 1-3.46 0" /></svg>', unread: 2 },
  { label: '评论与@', value: 'comments', icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" /></svg>', unread: 5 },
  { label: '赞和收藏', value: 'likes', icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" /></svg>' },
  { label: '私信', value: 'private', icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" /></svg>', unread: 1 },
  { label: '关注动态', value: 'follow', icon: '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" /><circle cx="9" cy="7" r="4" /><path d="M23 21v-2a4 4 0 0 0-3-3.87" /><path d="M16 3.13a4 4 0 0 1 0 7.75" /></svg>' }
]

const systemMessages = ref([
  { id: 1, title: '举报审核结果', content: '您举报的内容已被处理，感谢您的反馈。', time: '10分钟前', read: false },
  { id: 2, title: '订单状态更新', content: '您购买的商品"iPad Pro"已发货，请注意查收。', time: '1小时前', read: false },
  { id: 3, title: '系统维护通知', content: '系统将于今晚22:00-23:00进行维护，届时部分功能可能无法使用。', time: '昨天', read: true }
])

const commentMessages = ref([
  { id: 1, user: { name: '李同学', avatar: '' }, action: '评论了你的帖子', quote: '写得很好，学习了！', time: '30分钟前', read: false },
  { id: 2, user: { name: '王同学', avatar: '' }, action: '回复了你的评论', quote: '谢谢分享，很有帮助', time: '1小时前', read: false }
])

const likeMessages = ref([
  { id: 1, user: { name: '张同学', avatar: '' }, action: '赞', type: '帖子', title: '关于图书馆自习室预约系统的使用心得分享', time: '10分钟前', read: false },
  { id: 2, user: { name: '刘同学', avatar: '' }, action: '收藏', type: '帖子', title: '考研经验分享：如何高效备考数学', time: '2小时前', read: true }
])

const chatList = ref([
  {
    id: 1,
    user: { name: '陈同学', avatar: '' },
    lastMessage: '好的，那我们明天见面交易',
    unread: true,
    messages: [
      { id: 1, role: 'other', content: '你好，iPad还在吗？', time: '14:30' },
      { id: 2, role: 'me', content: '在的，你有兴趣吗？', time: '14:32' },
      { id: 3, role: 'other', content: '是的，可以便宜点吗？', time: '14:35' },
      { id: 4, role: 'me', content: '最低4200，明天可以当面交易', time: '14:38' },
      { id: 5, role: 'other', content: '好的，那我们明天见面交易', time: '14:40' }
    ]
  },
  {
    id: 2,
    user: { name: '周同学', avatar: '' },
    lastMessage: '收到，谢谢！',
    unread: false,
    messages: [
      { id: 1, role: 'me', content: '书已经给你放图书馆了', time: '10:00' },
      { id: 2, role: 'other', content: '收到，谢谢！', time: '10:05' }
    ]
  }
])

const followMessages = ref([
  { id: 1, user: { name: '张同学', avatar: '' }, type: '帖子', title: '食堂新推出的菜品测评，强烈推荐！', time: '1小时前', read: false },
  { id: 2, user: { name: '李同学', avatar: '' }, type: '商品', title: 'AirPods Pro 2代 全新未拆封', time: '3小时前', read: false }
])

const currentChat = computed(() => chatList.value.find(c => c.id === activeChat.value))

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesListRef.value) {
      messagesListRef.value.scrollTop = messagesListRef.value.scrollHeight
    }
  })
}

watch(activeChat, () => {
  scrollToBottom()
})

const sendChatMessage = () => {
  if (!chatInput.value.trim() || !activeChat.value) return
  
  const chat = chatList.value.find(c => c.id === activeChat.value)
  if (chat) {
    chat.messages.push({
      id: Date.now(),
      role: 'me',
      content: chatInput.value,
      time: new Date().toLocaleTimeString().slice(0, 5)
    })
    chat.lastMessage = chatInput.value
    chatInput.value = ''
    scrollToBottom()
  }
}
</script>

<style lang="scss" scoped>
.message-page {
  height: calc(100vh - 64px);
  background: $bg-secondary;
  overflow: hidden;
}

.message-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 220px 1fr;
  height: 100%;
}

.message-sidebar {
  background: $bg-primary;
  border-right: 1px solid $border-color;
}

.sidebar-header {
  padding: $spacing-lg;
  border-bottom: 1px solid $border-color;
  
  h2 {
    font-size: $font-size-lg;
    font-weight: $font-weight-semibold;
    color: $text-primary;
    margin: 0;
  }
}

.message-nav {
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

.nav-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  color: $text-muted;
  
  .nav-item-active & {
    color: $primary-color;
  }
}

.nav-label {
  flex: 1;
  text-align: left;
}

.nav-badge {
  min-width: 18px;
  height: 18px;
  padding: 0 6px;
  font-size: $font-size-xs;
  color: $text-white;
  background: $danger-color;
  border-radius: $radius-full;
}

.message-main {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.message-list {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.list-header {
  flex-shrink: 0;
  padding: $spacing-lg;
  border-bottom: 1px solid $border-color;
  
  h3 {
    font-size: $font-size-lg;
    font-weight: $font-weight-semibold;
    color: $text-primary;
    margin: 0;
  }
}

.list-content {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.message-item {
  display: flex;
  gap: $spacing-md;
  padding: $spacing-lg;
  border-bottom: 1px solid $border-light;
  cursor: pointer;
  transition: background $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
  
  &-unread {
    background: $primary-bg;
    
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 3px;
      background: $primary-color;
    }
  }
}

.message-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: $radius-full;
  
  &.system {
    background: $primary-bg;
    color: $primary-color;
  }
}

.message-content {
  flex: 1;
  
  h4 {
    font-size: $font-size-sm;
    font-weight: $font-weight-medium;
    color: $text-primary;
    margin: 0 0 $spacing-xs;
  }
  
  p {
    font-size: $font-size-sm;
    color: $text-secondary;
    margin: 0;
    
    strong {
      color: $primary-color;
    }
  }
}

.message-quote {
  padding: $spacing-sm;
  background: $bg-tertiary;
  border-radius: $radius-sm;
  margin-top: $spacing-xs !important;
}

.message-time {
  display: block;
  font-size: $font-size-xs;
  color: $text-light;
  margin-top: $spacing-xs;
}

.chat-view {
  flex: 1;
  display: grid;
  grid-template-columns: 280px 1fr;
  height: 100%;
  overflow: hidden;
}

.chat-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-right: 1px solid $border-color;
}

.chat-users {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.chat-user {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-md $spacing-lg;
  cursor: pointer;
  transition: background $transition-fast;
  
  &:hover {
    background: $bg-tertiary;
  }
  
  &-active {
    background: $primary-bg;
  }
}

.chat-info {
  flex: 1;
  min-width: 0;
}

.chat-name {
  display: block;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
}

.chat-preview {
  display: block;
  font-size: $font-size-xs;
  color: $text-muted;
  
  @include truncate(1);
}

.chat-badge {
  width: 8px;
  height: 8px;
  background: $danger-color;
  border-radius: 50%;
}

.chat-content {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.chat-header {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-md $spacing-lg;
  border-bottom: 1px solid $border-color;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
}

.messages-list {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: $spacing-lg;
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.chat-message {
  max-width: 70%;
  
  &-me {
    align-self: flex-end;
    
    p {
      background: $primary-color;
      color: $text-white;
      border-radius: $radius-lg $radius-lg 0 $radius-lg;
    }
    
    .msg-time {
      text-align: right;
    }
  }
  
  &-other {
    align-self: flex-start;
    
    p {
      background: $bg-tertiary;
      color: $text-primary;
      border-radius: $radius-lg $radius-lg $radius-lg 0;
    }
  }
  
  p {
    padding: $spacing-sm $spacing-md;
    margin: 0;
  }
}

.msg-time {
  display: block;
  font-size: $font-size-xs;
  color: $text-light;
  margin-top: 2px;
}

.chat-input {
  flex-shrink: 0;
  display: flex;
  gap: $spacing-sm;
  padding: $spacing-md $spacing-lg;
  border-top: 1px solid $border-color;
  background: $bg-primary;
  
  input {
    flex: 1;
    padding: $spacing-sm $spacing-md;
    font-size: $font-size-sm;
    background: $bg-tertiary;
    border: none;
    border-radius: $radius-full;
    outline: none;
    
    &:focus {
      background: $bg-secondary;
    }
  }
}
</style>
