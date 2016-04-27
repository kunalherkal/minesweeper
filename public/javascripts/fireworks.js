$(document).ready(function(){

const 
    PARTICLES=125;
    
// now we will setup our basic variables for the demo
var canvas = document.getElementById( 'fireworks' ),
    cw = canvas.width,
    ch = canvas.height,
    ctx = canvas.getContext( '2d' ),
    
    // firework collection
    fireworks = [],
    
    // particle collection
    particles = [],
    
    // starting hue
    hue = 120;

// get a random number within a range
function random( min, max ) {
    return Math.random() * ( max - min ) + min;
}

// calculate the distance between two points
function calculateDistance( p1x, p1y, p2x, p2y ) {
    var xDistance = p1x - p2x,
        yDistance = p1y - p2y;
    return Math.sqrt( Math.pow( xDistance, 2 ) + Math.pow( yDistance, 2 ) );
}

// create firework
function Firework( sx, sy, tx, ty ) {
    // actual coordinates
    this.x = sx;
    this.y = sy;
    // starting coordinates
    this.sx = sx;
    this.sy = sy;
    // target coordinates
    this.tx = tx;
    this.ty = ty;
    // distance from starting point to target
    this.distanceToTarget = calculateDistance( sx, sy, tx, ty );
    this.distanceTraveled = 0;
    // track the past coordinates of each firework to create a trail effect, increase the coordinate count to create more prominent trails
    this.coordinates = [];
    this.coordinateCount = 3;
    this.coordIndex = 0;
    this.coordOldest = 0;
    var c = this.coordinateCount;
    // populate initial coordinate collection with the current coordinates
    while( c-- ) {
        this.coordinates.push( [ this.x, this.y ] );
    }
    this.angle = Math.atan2( ty - sy, tx - sx );
    this.speed = 2;
    this.acceleration = 1.05;
    this.brightness = random( 50, 70 );
    // circle target indicator radius
    this.targetRadius = 1;
}

// update firework
Firework.prototype.update = function( index ) {
    // update the oldest coordinate, wrap around..
    this.coordIndex = (this.coordIndex + 1) % this.coordinateCount;
    this.coordinates[this.coordIndex][0] = this.x;
    this.coordinates[this.coordIndex][1] = this.y;
    
    // cycle the circle target indicator radius
    if( this.targetRadius < 8 ) {
        this.targetRadius += 0.3;
    } else {
        this.targetRadius = 1;
    }
    
    // speed up the firework
    this.speed *= this.acceleration;
    
    // get the current velocities based on angle and speed
    var vx = (Math.cos( this.angle ) * this.speed) | 0,
        vy = (Math.sin( this.angle ) * this.speed) | 0;
        
    // how far will the firework have traveled with velocities applied?
    this.distanceTraveled = calculateDistance( this.sx, this.sy, this.x + vx, this.y + vy );
    
    // if the distance traveled, including velocities, is greater than the initial distance to the target, then the target has been reached
    if( this.distanceTraveled >= this.distanceToTarget ) {
        createParticles( this.tx, this.ty );
        // remove the firework, use the index passed into the update function to determine which to remove
        fireworks.splice( index, 1 );
    } else {
        // target not reached, keep traveling
        this.x += vx;
        this.y += vy ;
    }
}

// draw firework
Firework.prototype.draw = function() {
    ctx.beginPath();
    // move to the last tracked coordinate in the set, then draw a line to the current x and y
    this.coordOldest = (this.coordIndex + 1) % this.coordinateCount; // wrap
    ctx.moveTo( this.coordinates[ this.coordOldest ][ 0 ], this.coordinates[ this.coordOldest ][ 1 ] );
    ctx.lineTo( this.x, this.y );
    ctx.strokeStyle = 'hsl(' + hue + ', 100%, ' + this.brightness + '%)';
    ctx.stroke();
}

// create particle
function Particle( x, y ) {
    this.x = x;
    this.y = y;
    // track the past coordinates of each particle to create a trail effect, increase the coordinate count to create more prominent trails
    this.coordinates = [];
    this.coordinateCount = 8;
    this.coordIndex = 0;
    this.coordOldest = 0;
    var c = this.coordinateCount;
    
    while( c-- ) {
        this.coordinates.push( [ this.x, this.y ] );
    }
    // set a random angle in all possible directions, in radians
    this.angle = random( 0, Math.PI * 2 );
    this.speed = random( 1, 10 );
    // friction will slow the particle down
    this.friction = 0.95;
    // gravity will be applied and pull the particle down
    this.gravity = 1;
    // set the hue to a random number +-20 of the overall hue variable
    this.hue = random( hue - 20, hue + 20 );
    this.brightness = random( 50, 80 );
    this.alpha = 1;
    // set how fast the particle fades out
    this.decay = random( 0.025, 0.05 );
}

// update particle
Particle.prototype.update = function( index ) {
    // update the oldest coordinate, wrap around..
    this.coordIndex = (this.coordIndex + 1) % this.coordinateCount;
    this.coordinates[this.coordIndex][0] = this.x;
    this.coordinates[this.coordIndex][1] = this.y;
    
    // slow down the particle
    this.speed *= this.friction;
    // apply velocity
    this.x += (Math.cos( this.angle ) * this.speed)| 0;
    this.y += (Math.sin( this.angle ) * this.speed + this.gravity) | 0;
    // fade out the particle
    this.alpha -= this.decay;
    
    // remove the particle once the alpha is low enough, based on the passed in index
    if( this.alpha <= this.decay ) {
        particles.splice( index, 1 );
    }
}

// draw particle
Particle.prototype.draw = function() {
    ctx. beginPath();
    // move to the last tracked coordinates in the set, then draw a line to the current x and y
    this.coordOldest = (this.coordIndex + 1) % this.coordinateCount; // wrap
    ctx.moveTo( this.coordinates[ this.coordOldest ][ 0 ], this.coordinates[ this.coordOldest ][ 1 ] );
    ctx.lineTo( this.x, this.y );
    ctx.strokeStyle = 'hsla(' + this.hue + ', 100%, ' + this.brightness + '%, ' + this.alpha + ')';
    ctx.stroke();
}

// create particle group/explosion
function createParticles( x, y ) {
    // increase the particle count for a bigger explosion, beware of the canvas performance hit with the increased particles though
    var particleCount = PARTICLES;
    while( particleCount-- ) {
        particles.push( new Particle( x, y ) );
    }
}
 
// lighter creates bright highlight points as the fireworks and particles overlap each other
ctx.globalCompositeOperation = 'lighter'; 
ctx.lineWidth = 2;
// main demo loop
function loop() {
    // this function will run endlessly with requestAnimationFrame
    if (fireworks.length === 0 && particles.length === 0) {
        return;
    }
    requestAnimationFrame( loop );  
    
    /* 
     normally, clearRect() would be used to clear the canvas
     we want to create a trailing effect though
     setting the composite operation to destination-out will allow us to clear the 
     canvas at a specific opacity, rather than wiping it entirely
    */
    ctx.save();
    ctx.globalCompositeOperation = 'destination-out';
    // decrease the alpha property to create more prominent trails
    ctx.fillStyle = 'rgba(0, 0, 0, 0.5)';
    ctx.fillRect( 0, 0, cw, ch );
    ctx.restore();
    // change the composite operation back to our main mode

    // increase the hue to get different colored fireworks over time
    hue = (hue + 0.5) % 360;

    // loop over each firework, draw it, update it
    var i = fireworks.length;
    while( i-- ) {
        fireworks[ i ].draw();
        fireworks[ i ].update( i );
    }
    
    // loop over each particle, draw it, update it
    var i = particles.length;
    while( i-- ) {
        particles[ i ].draw();
        particles[ i ].update( i );
    }
}

function fireVolley() {
    var requireLoop = (fireworks.length === 0 && particles.length === 0), 
        h = Math.floor(ch/3);
    
    for(var i=-200; i <= 200; i+=100) { // -200 (from middle) to +200        
        fireworks.push( new Firework( random(cw/2 - 50, cw/2 + 50), ch, cw/2 + i, random(h-50,h+50 )));
    }
    
    if (requireLoop === true) {
        loop();
    }
}

function fireMultiple(remain, minTime, maxTime) {
    fireVolley();    
    setTimeout(function() {
        if (remain -1 != 0) {
            fireMultiple(remain -1, minTime, maxTime);
        }
    }, random(minTime, maxTime));
}

$('#fireworks_container').on('click', function() {
    console.log('hhhhhhh')
    fireMultiple(5, 200, 300);
});

});