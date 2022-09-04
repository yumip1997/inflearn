<template>
  <div>
    <header>
        <h1> {{$route.name}} 페이지 입니다.</h1>
        <NuxtLink to = "/">홈페이지</NuxtLink>
        <NuxtLink to ="/AppMain">메인 페이지</NuxtLink>
        <NuxtLink to ="/AppProduct">상품 페이지</NuxtLink>
    </header>
    <Nuxt/>
  </div>
</template>

<script>
export default {

}
</script>

<style>

</style>