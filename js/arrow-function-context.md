When we implement a promise callback (backend call, for example), we don't need to assign `this` to another variable:

```es6
var self = this
axios.get(uri).then(response => {
  self.items = response.data.result.backendItems
})
```
In [arrow functions](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Functions/Arrow_functions#Syntax) the `this` variable is from the enclosing scope, that means we can use this normally inside the function block, such as:
```es6
// var self = this
axios.get(uri).then(response => {
//-  self.items = response.data.result.backendItems
  this.items = response.data.result.backendItems
})
```
