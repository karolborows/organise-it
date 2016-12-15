(function() {
    'use strict';

angular
    .module('organiseItApp')
    .directive('calculator', calculator);

/*Create a custom directive */
function calculator() {
    var dir = {};
    dir.restrict   = 'E'; /* restrict this directive to elements */
    dir.controller = 'CalcuController'; /* controller this directive function belongs */
    dir.template   = '<div  class="calculator demo">'
        +'<div class="u4 display">'
        +'<div class="display-inner">{{out}}</div>'
        +'</div>'
        +'<button ng-repeat="calkey in mykeys track by $index" ng-click="display(calkey)" '
        +'ng-class="{\'u2\': calkey == \'0\' || calkey == \'<-\', \'button-blue\' : calkey == \'=\' , \'button-red\' : calkey == \'c\' }"'
        +'class="u1 button button-gray" >'
        +'<div ng-if="calkey!=\'<-\'">{{calkey}}</div>'
        +'<div ng-if="calkey==\'<-\'">Backspace</div>'
        +'</button>'
        +'</div>';
    //dir.templateUrl = 'calculator.html'; //
    return dir;
}
})();


