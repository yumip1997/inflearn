import {fetchCartItems} from "~/api/cart/CartApi";

export const FETCH_CART_ITEMS = 'FETCH_CART_ITEMS'

export const state = () => ({
  cartItems : [],
})

export const mutations = {
  addCartItem(state , cartItem){
    const convertedCartItem = {
      ...cartItem,
      imageUrl : `${cartItem.imageUrl}?random=${Math.random()}`
    }
    state.cartItems.push(convertedCartItem)
  },
  setCartItems(state, cartItems){
    state.cartItems = cartItems
  }
}


export const actions = {
  async [FETCH_CART_ITEMS]({commit}) {
    const userId = "1"
    const { data } = await fetchCartItems(userId)
    console.log(data)
    commit(
      'setCartItems',
      data.products
    )
  },
  async nuxtServerInit(storeContext, nuxtContext){
    await storeContext.dispatch(FETCH_CART_ITEMS)
  }
}

export const getters = {
  cartItems(state){
    return state.cartItems
  }
}


