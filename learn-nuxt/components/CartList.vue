<template>
  <div class="list-wrapper">
    <ul>
      <li v-for="(cartItem) in cartItems"
          :key="cartItem.id"
          class="list-item">
        <div class="description">
          <p>{{ cartItem.title }}</p>
          <span>$ {{ cartItem.price }}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import {FETCH_CART_ITEMS} from "~/store"
import {mapGetters} from 'vuex'

export default {
  name: "CartList",
  computed :{
    ...mapGetters(['cartItems'])
  },
  async fetch(){
    await this.$store.dispatch(FETCH_CART_ITEMS)
  },
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
