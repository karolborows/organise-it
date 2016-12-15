/**
 * Created by kborowski on 2016-12-04.
 */
(function () {
    'use strict';

    angular
        .module('organiseItApp')
        .factory('MathNumbers', MathNumbers);
     function MathNumbers() {
        var factory = {};

        factory.calcnumbers = function () {
            var numbs = ['c', '<-', '/',
                '7', '8', '9', '+',
                '4', '5', '6', '-',
                '1', '2', '3', '*',
                '0', '.', '='];

            return numbs;
        }
        return factory;
    }

})();
