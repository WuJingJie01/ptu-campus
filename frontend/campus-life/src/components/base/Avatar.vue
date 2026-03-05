<template>
  <div
    :class="['avatar', `avatar-${size}`, { 'avatar-clickable': clickable }]"
    :style="{ backgroundColor: bgColor }"
    @click="handleClick"
  >
    <img
      v-if="src && !imageError"
      :src="src"
      :alt="alt"
      class="avatar-image"
      @error="handleImageError"
    />
    <span v-else class="avatar-text">{{ displayText }}</span>
    <span v-if="online" class="avatar-online"></span>
  </div>
</template>

<script setup lang="ts">
interface Props {
  src?: string
  alt?: string
  name?: string
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
  online?: boolean
  clickable?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  src: '',
  alt: '',
  name: '',
  size: 'md',
  online: false,
  clickable: false,
})

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const imageError = ref(false)

const displayText = computed(() => {
  if (props.name) {
    return props.name.slice(0, 2)
  }
  return 'U'
})

const bgColor = computed(() => {
  if (props.src && !imageError.value) return 'transparent'
  const colors = ['#4A90D9', '#5CB85C', '#FF9500', '#E74C3C', '#9B59B6', '#3498DB']
  const index = props.name ? props.name.charCodeAt(0) % colors.length : 0
  return colors[index]
})

const handleImageError = () => {
  imageError.value = true
}

const handleClick = (event: MouseEvent) => {
  if (props.clickable) {
    emit('click', event)
  }
}
</script>

<style lang="scss" scoped>
.avatar {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: $radius-full;
  overflow: hidden;
  font-weight: $font-weight-semibold;
  color: $text-white;
  flex-shrink: 0;
  
  &-xs {
    width: 24px;
    height: 24px;
    font-size: $font-size-xs;
  }
  
  &-sm {
    width: 32px;
    height: 32px;
    font-size: $font-size-xs;
  }
  
  &-md {
    width: 40px;
    height: 40px;
    font-size: $font-size-sm;
  }
  
  &-lg {
    width: 48px;
    height: 48px;
    font-size: $font-size-base;
  }
  
  &-xl {
    width: 64px;
    height: 64px;
    font-size: $font-size-lg;
  }
  
  &-clickable {
    cursor: pointer;
    transition: transform $transition-fast;
    
    &:hover {
      transform: scale(1.05);
    }
  }
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-text {
  text-transform: uppercase;
}

.avatar-online {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 25%;
  height: 25%;
  min-width: 8px;
  min-height: 8px;
  background: $success-color;
  border: 2px solid $bg-primary;
  border-radius: $radius-full;
}
</style>
