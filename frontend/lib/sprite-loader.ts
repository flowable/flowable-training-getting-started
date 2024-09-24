const isLoaded = document.querySelector('#svgicon-address');

let cachedSvgSprite = null;
function insertSvg(svgSprite) {
    const loadedSvgSprite = svgSprite || cachedSvgSprite;
    if (document.readyState !== 'loading' && loadedSvgSprite) {
        // place the svg into the dom if it's ready
        document.body.insertBefore(loadedSvgSprite, document.body.childNodes[0]);
        cachedSvgSprite = null;
    } else if (svgSprite) {
        // cache sprite until the dom is ready
        cachedSvgSprite = svgSprite;
    }
}

// only loads the svg if the dom has been fully loaded
if (!isLoaded && !window['spritesLoading']) {
    window['spritesLoading'] = true;
    // load svg icon sprites async and place them in dom for older browsers
    const spriteRequest = new XMLHttpRequest();
    spriteRequest.addEventListener('load', function placeInDOM() {
        const div = document.createElement('div');
        div.style.height = '0px';
        div.style.width = '0px';
        div.innerHTML = this.responseText;
        insertSvg(div);
    });
    spriteRequest.open('GET', `https://static.ag.ch/global/releases/v14.9.1/images/sprites.svg`, true);
    spriteRequest.send();
}

export default isLoaded;