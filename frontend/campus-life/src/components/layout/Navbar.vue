<template>
  <header class="navbar" :class="{ 'navbar-scrolled': isScrolled }">
    <div class="navbar-container">
      <router-link to="/" class="navbar-logo">
        <div class="navbar-logo-icon">
          <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
          </svg>
        </div>
        <span class="navbar-logo-text">莆田学院校园生活</span>
      </router-link>

      <nav class="navbar-menu">
        <router-link
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          class="navbar-menu-item"
          :class="{ 'navbar-menu-item-active': isActiveRoute(item.path) }"
        >
          {{ item.label }}
        </router-link>
      </nav>

      <div class="navbar-actions">
        <div class="navbar-search">
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索帖子、商品、兼职..."
            class="navbar-search-input"
            @keyup.enter="handleSearch"
          />
          <button class="navbar-search-btn" @click="handleSearch">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8" />
              <path d="M21 21l-4.35-4.35" />
            </svg>
          </button>
        </div>

        <router-link to="/message" class="navbar-icon-btn">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
            <path d="M13.73 21a2 2 0 0 1-3.46 0" />
          </svg>
          <span v-if="unreadCount > 0" class="navbar-badge"></span>
        </router-link>

        <router-link v-if="userStore.isAuth" to="/content" class="navbar-icon-btn">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
            <polyline points="14 2 14 8 20 8" />
            <line x1="16" y1="13" x2="8" y2="13" />
            <line x1="16" y1="17" x2="8" y2="17" />
            <polyline points="10 9 9 9 8 9" />
          </svg>
        </router-link>

        <Dropdown v-if="userStore.isAuth" placement="bottom-end">
          <template #trigger>
            <div class="navbar-publish-btn">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              <span>发布</span>
            </div>
          </template>
          <DropdownItem @click="router.push('/forum/post')">
            <template #icon>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
              </svg>
            </template>
            发帖子
          </DropdownItem>
          <DropdownItem @click="router.push('/market/post')">
            <template #icon>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2" />
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" />
              </svg>
            </template>
            发商品
          </DropdownItem>
          <DropdownItem @click="router.push('/job/post')">
            <template #icon>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2" />
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" />
              </svg>
            </template>
            发兼职
          </DropdownItem>
        </Dropdown>

        <Dropdown v-if="userStore.isAuth" placement="bottom-end">
          <template #trigger>
            <Avatar
              :src="userStore.user?.avatar"
              :name="userStore.user?.nickname"
              size="md"
              clickable
            />
          </template>
          <div class="navbar-user-info">
            <div class="navbar-user-name">{{ userStore.user?.nickname }}</div>
            <Badge v-if="userStore.user?.isVerified" type="primary" size="sm">已认证</Badge>
          </div>
          <div class="navbar-dropdown-divider"></div>
          <DropdownItem @click="router.push(`/user/${userStore.user?.id}`)">
            <template #icon>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
            </template>
            个人主页
          </DropdownItem>
          <DropdownItem @click="router.push('/user/settings')">
            <template #icon>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="3" />
                <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z" />
              </svg>
            </template>
            个人设置
          </DropdownItem>
          <DropdownItem @click="handleLogout">
            <template #icon>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
                <polyline points="16 17 21 12 16 7" />
                <line x1="21" y1="12" x2="9" y2="12" />
              </svg>
            </template>
            退出登录
          </DropdownItem>
        </Dropdown>

        <router-link v-else to="/login" class="navbar-login-btn">
          登录
        </router-link>
      </div>

      <button class="navbar-mobile-toggle" @click="isMobileMenuOpen = !isMobileMenuOpen">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="3" y1="12" x2="21" y2="12" />
          <line x1="3" y1="6" x2="21" y2="6" />
          <line x1="3" y1="18" x2="21" y2="18" />
        </svg>
      </button>
    </div>

    <Transition name="mobile-menu">
      <div v-if="isMobileMenuOpen" class="navbar-mobile-menu">
        <nav class="navbar-mobile-nav">
          <router-link
            v-for="item in menuItems"
            :key="item.path"
            :to="item.path"
            class="navbar-mobile-item"
            @click="isMobileMenuOpen = false"
          >
            {{ item.label }}
          </router-link>
        </nav>
      </div>
    </Transition>
  </header>
</template>

<script setup lang="ts">
import { Avatar, Dropdown, DropdownItem, Badge } from '@/components/base'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const searchKeyword = ref('')
const isScrolled = ref(false)
const isMobileMenuOpen = ref(false)
const unreadCount = ref(3)

const menuItems = [
  { path: '/forum', label: '校园论坛' },
  { path: '/market', label: '校园市场' },
  { path: '/job', label: '校园兼职' },
  { path: '/ai-assistant', label: 'AI学长' },
]

const isActiveRoute = (path: string) => {
  return route.path.startsWith(path)
}

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/search', query: { q: searchKeyword.value.trim() } })
    searchKeyword.value = ''
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}

onMounted(() => {
  window.addEventListener('scroll', () => {
    isScrolled.value = window.scrollY > 10
  })
})

onUnmounted(() => {
  window.removeEventListener('scroll', () => {})
})
</script>

<style lang="scss" scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  background: rgba($bg-primary, 0.95);
  backdrop-filter: blur(8px);
  border-bottom: 1px solid transparent;
  z-index: $z-index-fixed;
  transition: all $transition-base;
  
  &-scrolled {
    border-bottom-color: $border-color;
    box-shadow: $shadow-sm;
  }
}

.navbar-container {
  max-width: 1280px;
  height: 100%;
  margin: 0 auto;
  padding: 0 $spacing-lg;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.navbar-logo {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  color: $text-primary;
  text-decoration: none;
  
  &-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    background: linear-gradient(135deg, $primary-color, $primary-light);
    border-radius: $radius-lg;
    color: $text-white;
  }
  
  &-text {
    font-size: $font-size-lg;
    font-weight: $font-weight-semibold;
    
    @include respond-to(md) {
      display: none;
    }
    
    @include respond-to(lg) {
      display: block;
    }
  }
}

.navbar-menu {
  display: none;
  align-items: center;
  gap: $spacing-xs;
  
  @include respond-to(lg) {
    display: flex;
  }
  
  &-item {
    padding: $spacing-sm $spacing-md;
    font-size: $font-size-sm;
    font-weight: $font-weight-medium;
    color: $text-secondary;
    text-decoration: none;
    border-radius: $radius-md;
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
}

.navbar-actions {
  display: flex;
  align-items: center;
  gap: $spacing-md;
}

.navbar-search {
  position: relative;
  display: none;
  
  @include respond-to(md) {
    display: block;
  }
  
  &-input {
    width: 240px;
    height: 36px;
    padding: 0 $spacing-xl 0 $spacing-md;
    font-size: $font-size-sm;
    background: $bg-tertiary;
    border: 1px solid transparent;
    border-radius: $radius-full;
    outline: none;
    transition: all $transition-fast;
    
    &:focus {
      width: 280px;
      background: $bg-primary;
      border-color: $primary-color;
    }
    
    &::placeholder {
      color: $text-light;
    }
  }
  
  &-btn {
    position: absolute;
    right: $spacing-sm;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    justify-content: center;
    width: 28px;
    height: 28px;
    background: transparent;
    border: none;
    color: $text-muted;
    cursor: pointer;
    border-radius: $radius-full;
    transition: all $transition-fast;
    
    &:hover {
      color: $primary-color;
      background: $primary-bg;
    }
  }
}

.navbar-icon-btn {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  color: $text-secondary;
  border-radius: $radius-full;
  transition: all $transition-fast;
  
  &:hover {
    color: $primary-color;
    background: $primary-bg;
  }
}

.navbar-badge {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 8px;
  height: 8px;
  background: $danger-color;
  border-radius: $radius-full;
}

.navbar-publish-btn {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-white;
  background: $primary-color;
  border-radius: $radius-full;
  cursor: pointer;
  transition: all $transition-fast;
  
  &:hover {
    background: $primary-dark;
  }
}

.navbar-login-btn {
  padding: $spacing-sm $spacing-lg;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $primary-color;
  background: transparent;
  border: 1px solid $primary-color;
  border-radius: $radius-full;
  text-decoration: none;
  transition: all $transition-fast;
  
  &:hover {
    color: $text-white;
    background: $primary-color;
  }
}

.navbar-user-info {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-sm $spacing-md;
}

.navbar-user-name {
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
}

.navbar-dropdown-divider {
  height: 1px;
  background: $border-color;
  margin: $spacing-xs 0;
}

.navbar-mobile-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: transparent;
  border: none;
  color: $text-secondary;
  cursor: pointer;
  border-radius: $radius-md;
  
  @include respond-to(lg) {
    display: none;
  }
  
  &:hover {
    background: $bg-tertiary;
  }
}

.navbar-mobile-menu {
  position: absolute;
  top: 64px;
  left: 0;
  right: 0;
  background: $bg-primary;
  border-bottom: 1px solid $border-color;
  box-shadow: $shadow-lg;
  padding: $spacing-md;
  
  @include respond-to(lg) {
    display: none;
  }
}

.navbar-mobile-nav {
  display: flex;
  flex-direction: column;
  gap: $spacing-xs;
}

.navbar-mobile-item {
  display: block;
  padding: $spacing-md;
  font-size: $font-size-base;
  color: $text-secondary;
  text-decoration: none;
  border-radius: $radius-md;
  
  &:hover {
    color: $primary-color;
    background: $primary-bg;
  }
}

.mobile-menu-enter-active,
.mobile-menu-leave-active {
  transition: all 0.2s ease;
}

.mobile-menu-enter-from,
.mobile-menu-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
