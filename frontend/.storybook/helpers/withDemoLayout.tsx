import React from 'react';
import '../../lib/index.scss';

const withDemolayout = story => (
    <div className="demolayout richtext">
        {story()}
    </div>
);

export default withDemolayout;

export {
    withDemolayout
}
