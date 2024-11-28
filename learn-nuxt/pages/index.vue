<template>
  <div class="app">
    <main>
      <div>
        <SearchInput v-model="searchKeyword" @search="searchProducts"/>
      </div>
      <div>
        <ProductList :product-list="products" />
      </div>
      <div class="cart-wrapper">
        <button class="btn" @click="moveToCart">장바구니 바로가기</button>
      </div>
    </main>
  </div>
</template>

<script>
import {fetchProducts, fetchProductsByKeyword} from "~/api/product/ProductApi";

export default {
  name: 'IndexPage',

  async asyncData() {
    const { data } =  await fetchProducts()
    return {
      products : data.products
    }
  },

  data() {
    return {
      searchKeyword: '',
    }
  },

  methods: {
    async searchProducts() {
      const { data } = await fetchProductsByKeyword(this.searchKeyword)
      this.products = data.products
    },
    moveToCart() {
      this.$router.push("/AppCart");
    }
  }
}
</script>

<style scoped>
.app {
  position: relative;
}

.cart-wrapper {
  position: sticky;
  float: right;
  bottom: 50px;
  right: 50px;
}

.cart-wrapper .btn {
  display: inline-block;
  height: 40px;
  font-size: 1rem;
  font-weight: 500;
}
</style>
