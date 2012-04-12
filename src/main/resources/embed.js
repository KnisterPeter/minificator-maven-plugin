var timers = [];
var window = {
  document : {
    getElementById : function(id) {
      return [];
    },
    getElementsByTagName : function(tagName) {
      return [];
    }
  },
  location : {
    protocol : 'file:',
    hostname : 'localhost',
    port : '80'
  },
  setInterval : function(fn, time) {
    var num = timers.length;
    timers[num] = fn.call(this, null);
    return num;
  }
};
var document = window.document;
var location = window.location;
var setInterval = window.setInterval;

var compileFile = function(file) {
  var result = null;
  var charset = 'UTF-8';
  var dirname = file.replace(/\\/g, '/').replace(/[^\/]+$/, '');

  window.less.Parser.importer = function(path, paths, fn) {
    new (window.less.Parser)({
      optimization : 3
    }).parse(readUrl(dirname + path, charset), function(e, root) {
      fn(root);
    });
  };
  new (window.less.Parser)({
    optimization : 3
  }).parse(readUrl(file, charset), function(e, root) {
    result = root.toCSS();
  });
  return result;
};
