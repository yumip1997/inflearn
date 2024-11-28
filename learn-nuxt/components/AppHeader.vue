<template>
  <header class="header">
    <nuxt-link to="/"
               class="logo">
      Nuxt Shopping
    </nuxt-link>
  </header>
</template>

<script>
export default {
  name: "AppHeader"
}
</script>

<style scoped>
header {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 0.5rem;
}
.logo {
  font-weight: 900;
}
.logo:visited {
  color: inherit;
}
</style>
