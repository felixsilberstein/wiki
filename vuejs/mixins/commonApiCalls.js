import axios from 'axios/index'

export const commonApiCalls = {
  methods: {
    /**
     * Helper function to build URLs
     * @param path
     * @param paramsObject
     * @returns {string}
     */
    buildBackEndUrl (path, paramsObject) {
      if (Object.keys(paramsObject).length === 0 && paramsObject.constructor === Object) {
        return path
      } else {
        return path + '?' + Object
          .keys(paramsObject)
          .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(paramsObject[key])}`)
          .join('&')
      }
    },
    cancelableAxiosGet (...args) {
      const CancelToken = axios.CancelToken
      const source = CancelToken.source()
      const promise = axios.get(...args)
      return {
        promise, source
      }
    },
  }
}
