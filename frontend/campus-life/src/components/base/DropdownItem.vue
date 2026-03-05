<template>
  <button class="dropdown-item" :class="{ 'dropdown-item-disabled': disabled }" @click="handleClick">
    <span v-if="$slots.icon" class="dropdown-item-icon">
      <slot name="icon" />
    </span>
    <span class="dropdown-item-content">
      <slot />
    </span>
  </button>
</template>

<script setup lang="ts">
interface Props {
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  disabled: false,
})

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const dropdown = inject<{ close: () => void }>('dropdown', { close: () => {} })

const handleClick = (event: MouseEvent) => {
  if (!props.disabled) {
    emit('click', event)
    dropdown.close()
  }
}
</script>

<style lang="scss" scoped>
.dropdown-item {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  width: 100%;
  padding: $spacing-sm $spacing-md;
  font-size: $font-size-sm;
  color: $text-secondary;
  background: transparent;
  border: none;
  border-radius: $radius-md;
  cursor: pointer;
  transition: all $transition-fast;
  text-align: left;
  
  &:hover:not(.dropdown-item-disabled) {
    background: $bg-tertiary;
    color: $text-primary;
  }
  
  &-disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}

.dropdown-item-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  color: $text-muted;
}

.dropdown-item-content {
  flex: 1;
}
</style>
