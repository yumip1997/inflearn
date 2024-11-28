import {getApi, postApi} from "~/api/AxiosApi";

const url = "/carts"

const fetchCartItems = (userId) => getApi(`${url}/${userId}`)

const createCartItem = (cartItem) => postApi(url, cartItem)

export {fetchCartItems, createCartItem}
