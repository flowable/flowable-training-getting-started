import {FormLayout} from "@flowable/forms/flowable-forms/src/flw/Model";

export const defaultForm: FormLayout = {
    "outcomes": [
        {
            "id": "submit",
            "label": "Submit",
            "value": " SUBMIT"
        },
    ],
    "rows": [
        {
            "cols": [
                {
                    "labelAlign": "top",
                    "value": "No Form has been defined.",
                    "type": "htmlComponent"
                }
            ]
        },
    ],
}
