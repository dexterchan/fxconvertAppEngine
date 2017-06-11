/**
 * entry page control
 */

var app = angular.module('app', ['ngRoute','ngResource']);
app.config(function($routeProvider){
    $routeProvider
        .when('/allFX',{
            templateUrl: '/views/allFX.html',
            controller: 'allFXController'
        })
        .when('/calcFx',{
            templateUrl: '/views/calcFx.html',
            controller: 'calcFxController'
        })
        .otherwise(
            { redirectTo: '/'}
        );
});