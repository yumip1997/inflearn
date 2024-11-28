import {getApi} from "~/api/AxiosApi";

export const ApiMixin = {
  methods : {
    async getApi(url) {
      return await getApi(url)
    }
  }
}
