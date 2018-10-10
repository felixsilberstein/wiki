import Vue from 'vue'
Vue.directive('add-to-style-scope', {
  componentUpdated: function (el, binding, vnode) {
    Vue.nextTick(function () {
      var element = document.querySelector(binding.value)
      if (element) {
        for (var k of Object.keys(vnode.componentInstance.$el.dataset)) {
          element.dataset[k] = ''
        }
      }
    })
  }
})
