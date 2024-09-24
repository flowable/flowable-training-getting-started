import './index.scss';
import Components from './index-design';

const formComponents = Object.keys(Components).reduce((components, current) => {
    components.push([current, Components[current]]);
    return components;
}, [])

export default {formComponents};

