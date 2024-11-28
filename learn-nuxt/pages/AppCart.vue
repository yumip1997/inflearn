<template>
  <div class="container">
    <h1 class="list-title">카트 페이지입니다.</h1>
    <cart-list />
    <div>
      <button>구매하기</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "AppCart",
}
</script>

<style scoped>
.container {
  margin: 2rem 10rem;
}
.list-title {
  font-weight: 700;
  font-size: 1.4rem;
}
</style>
