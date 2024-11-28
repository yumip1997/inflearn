import axios from "axios";

const instance = axios.create({
  baseURL: process.env.baseURL,
})

const getApi = (url, param) => {
  return instance.get(url, makeConfig(param))
    .then(res => res)
    .catch(error => error)
}

const makeConfig = param => param ? {params : param} : null

const postApi = (url, param) => {
  return instance.post(url, param)
    .then(res => res)
    .catch(error => error)
}

export {getApi, postApi}
