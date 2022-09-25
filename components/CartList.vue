<template>
  <div class="list-wrapper">
    <ul>
      <li v-for="(cartItem) in $store.state.cartItems"
          :key="cartItem.id"
          class="list-item">
        <img :src="cartItem.imageUrl"
             :alt="cartItem.name"
             class="thumbnail">
        <div class="description">
          <p>{{ cartItem.name }}</p>
          <span>{{ cartItem.price }}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import {FETCH_CART_ITEMS} from "~/store";

export default {
  name: "CartList",
  async fetch(){
    const check = `${process.server}` === "true" ? "server" : "browser"
    console.log("fetch : " + check)
    await this.$store.dispatch(FETCH_CART_ITEMS)
  },
  // async created(){
  //   //첫 화면 진입시
  //   //server 쪽에서도 호출, browser 쪽에서도 호출
  //   const check = `${process.server}` === "true" ? "server" : "browser"
  //   console.log("created : " + check)
  //   await this.$store.dispatch(FETCH_CART_ITEMS)
  // }
}
</script>

<style scoped>
.list-wrapper {
  margin: 0.4rem 0;
}
.list-item {
  display: flex;
}
.thumbnail {
  width: 100px;
  height: 100px;
}
.description {
  padding: 2rem 1rem;
}
.extra-panel {
  text-align: right;
  padding: 0.2rem 0;
}
</style>
