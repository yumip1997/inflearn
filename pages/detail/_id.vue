<template>
  <div>
    <div class="container">
      <div class="main-panel">
        <img
          class="product-image"
          :src="product.thumbnail"
          :alt="product.title"
        />
      </div>
      <div class="side-panel">
        <p class="name">{{ product.title }}</p>
        <p class="price">$ {{product.price }}</p>
        <button type="button" @click="addToCart">Add to Cart</button>
      </div>
    </div>
  </div>
</template>

<script>
import {createCartItem} from "~/api/cart/CartApi";
import {fetchProductById} from "~/api/product/ProductApi";

export default {
  name: "_id.vue",

  async asyncData({params}){
    const {data} = await fetchProductById(params.id)
    return {
      product : data
    }
  },

  methods : {
    async addToCart(){
      await createCartItem(this.product)
      this.$store.commit('addCartItem', this.product)
      await this.$router.push('/AppCart')
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  margin: 2rem 0;
}
.product-image {
  width: 500px;
  height: 375px;
}
.side-panel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 220px;
  text-align: center;
  padding: 0 1rem;
}
</style>
