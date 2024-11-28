<template>
  <div class="input-wrapper flex">
    <input type="text"
           class="search-input"
           :value="value"
           @input="$emit('input', $event.target.value)"
    />
    <button type="button"
            class="btn"
            @click="$emit('search')">search</button>
  </div>
</template>

<script>
export default {
  name: "SearchInput",

  props: {
    value: {
      type: String,
      default: () => ''
    },
  },
}
</script>

<style scoped>
.input-wrapper {
  height: 40px;
  margin: 1rem 0;
}
.search-input {
  width: 200px;
  font-size: 1.2rem;
  font-weight: 500;
}
.btn {
  font-size: 1.2rem;
  font-weight: 500;
}
</style>
