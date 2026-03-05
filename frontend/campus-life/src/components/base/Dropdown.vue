<template>
  <div ref="dropdownRef" class="dropdown">
    <div class="dropdown-trigger" @click="handleToggle">
      <slot name="trigger" />
    </div>
    <Transition name="dropdown">
      <div
        v-if="isOpen"
        :class="['dropdown-menu', `dropdown-${placement}`]"
      >
        <slot />
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
interface Props {
  placement?: 'bottom-start' | 'bottom-end' | 'top-start' | 'top-end'
  closeOnClick?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  placement: 'bottom-start',
  closeOnClick: true,
})

const isOpen = ref(false)
const dropdownRef = ref<HTMLElement | null>(null)

const handleToggle = () => {
  isOpen.value = !isOpen.value
}

const close = () => {
  isOpen.value = false
}

const handleClickOutside = (event: MouseEvent) => {
  if (dropdownRef.value && !dropdownRef.value.contains(event.target as Node)) {
    close()
  }
}

provide('dropdown', {
  close: props.closeOnClick ? close : () => {},
})

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style lang="scss" scoped>
.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-trigger {
  cursor: pointer;
}

.dropdown-menu {
  position: absolute;
  background: $bg-primary;
  border: 1px solid $border-color;
  border-radius: $radius-lg;
  box-shadow: $shadow-lg;
  min-width: 180px;
  padding: $spacing-xs;
  z-index: $z-index-dropdown;
  
  &-bottom-start {
    top: 100%;
    left: 0;
    margin-top: $spacing-xs;
  }
  
  &-bottom-end {
    top: 100%;
    right: 0;
    margin-top: $spacing-xs;
  }
  
  &-top-start {
    bottom: 100%;
    left: 0;
    margin-bottom: $spacing-xs;
  }
  
  &-top-end {
    bottom: 100%;
    right: 0;
    margin-bottom: $spacing-xs;
  }
}

.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.15s ease;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
