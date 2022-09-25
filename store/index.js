import {fetchCartItems} from "~/api"

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
    const { data } = await fetchCartItems()
    commit(
      'setCartItems',
      data.map((item) => ({
        ...item,
        imageUrl : `${item.imageUrl}?random=${Math.random()}`
      }))
    )
  },
  async nuxtServerInit(storeContext, nuxtContext){
    await storeContext.dispatch(FETCH_CART_ITEMS)
  }
}

