import {getApi} from "~/api/AxiosApi";

const url = '/products'

const fetchProducts = () => getApi(url)

const fetchProductById = (id) => getApi(`${url}/${id}`)

const fetchProductsByKeyword = (keyword) => getApi(`${url}/search`, {q : keyword})

export {fetchProducts, fetchProductById, fetchProductsByKeyword}
