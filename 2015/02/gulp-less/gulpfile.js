var gulp = require('gulp'),
    less = require('gulp-less'),
    path = require('path'),
    watch = require('gulp-watch'),
    sourcemaps = require('gulp-sourcemaps'),
    less_paths = [],
    compiled_static_pats = [];

// paths where less files are allocated

less_paths = [
        __dirname + '/bootstrap-3.3.2/less/bootstrap.less'
    ];

// paths were to find for changes
watch_paths = [
    './bootstrap-3.3.2/less/*.less',
    './bootstrap-3.3.2/less/**/*.less',

]

//paths where statics files will be compiled
compiled_static_pats = [
    __dirname + '/public/css'
];


// Define default tasks
gulp.task('default', ['less:static']);


//Watcher to detect changes in less files
gulp.task('check', function() {
    gulp.watch(watch_paths, ['less:static']);
 });


gulp.task('less:static', function () {

    return gulp.src(less_paths)
        .pipe(sourcemaps.init())
        .pipe(less())
        .pipe(sourcemaps.write('./'))
        .pipe(gulp.dest(__dirname + '/public/css'));

});
