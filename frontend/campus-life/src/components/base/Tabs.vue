<template>
  <div class="tabs">
    <div class="tabs-list" role="tablist">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        :class="['tabs-tab', { 'tabs-tab-active': modelValue === tab.value }]"
        role="tab"
        :aria-selected="modelValue === tab.value"
        @click="handleTabClick(tab.value)"
      >
        <span v-if="tab.icon" class="tabs-tab-icon">
          <component :is="tab.icon" />
        </span>
        {{ tab.label }}
        <span v-if="tab.badge" class="tabs-tab-badge">{{ tab.badge }}</span>
      </button>
    </div>
    <div class="tabs-content">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
interface Tab {
  label: string
  value: string
  icon?: any
  badge?: number | string
}

interface Props {
  tabs: Tab[]
  modelValue: string
}

defineProps<Props>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
  change: [value: string]
}>()

const handleTabClick = (value: string) => {
  emit('update:modelValue', value)
  emit('change', value)
}
</script>

<style lang="scss" scoped>
.tabs {
  width: 100%;
}

.tabs-list {
  display: flex;
  gap: $spacing-xs;
  border-bottom: 1px solid $border-color;
  overflow-x: auto;
  
  &::-webkit-scrollbar {
    display: none;
  }
}

.tabs-tab {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  padding: $spacing-md $spacing-lg;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-muted;
  background: transparent;
  border: none;
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all $transition-fast;
  white-space: nowrap;
  
  &:hover {
    color: $text-secondary;
  }
  
  &-active {
    color: $primary-color;
    border-bottom-color: $primary-color;
  }
}

.tabs-tab-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
}

.tabs-tab-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 18px;
  padding: 0 6px;
  font-size: $font-size-xs;
  font-weight: $font-weight-medium;
  color: $text-white;
  background: $primary-color;
  border-radius: $radius-full;
}

.tabs-content {
  padding: $spacing-lg 0;
}
</style>
