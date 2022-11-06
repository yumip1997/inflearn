<template>
  <ul>
    <li v-for="product in productList"
        :key="product.id"
        class="item flex"
        @click="moveToDetailPage(product.id)">
      <img class="product-image" :src="product.thumbnail" :alt="product.title">
      <p class="name">{{ product.title }}</p>
      <p class="price">$ {{ product.price }}</p>
    </li>
  </ul>
</template>

<script>
export default {
  name: "ProductList",
  props: {
    productList : {
      type : Array,
      default : () => {}
    }
  },
  methods: {
    moveToDetailPage(id) {
      this.$router.push(`detail/${id}`)
    },
  }
}
</script>

<style scoped>
.flex {
  display: flex;
  justify-content: center;
}

.item {
  display: inline-block;
  width: 400px;
  height: 300px;
  text-align: center;
  margin: 0 0.5rem;
  cursor: pointer;
}

.product-image {
  width: 400px;
  height: 250px;
}
</style>
