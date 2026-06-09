import React from "react";
import type {CustomComponentProps} from "@flowable/forms";
import {useCustomComponent} from "@flowable/forms";
import "./PinInput.css";
import type {ExtraSettings} from "./generated-types.js";

export default function PinInput(props: CustomComponentProps<string, ExtraSettings>) {
    const {value = "", setValue, extraSettings, enabled} = useCustomComponent<string, ExtraSettings>();
    const length = extraSettings?.length ?? 4;
    const allowLetters = extraSettings?.allowLetters ?? false;
    const masked = extraSettings?.masked ?? true;

    function handleChange(index: number, e: React.ChangeEvent<HTMLInputElement>) {
        const char = allowLetters
            ? e.target.value.slice(-1)
            : e.target.value.replace(/\D/g, "").slice(-1);
        if (!char) return;

        const next = [...Array(length)].map((_, i) => value[i] ?? "");
        next[index] = char;
        setValue(next.join(""));

        (e.target.nextElementSibling as HTMLInputElement | null)?.focus();
    }

    const Label = props.Components?.label as any;

    return (
        <div>
            {Label && <Label {...props} />}
            <div className="pin-input">
                {[...Array(length)].map((_, i) => (
                    <input
                        key={i}
                        className="pin-input__box"
                        type={masked ? "password" : "text"}
                        inputMode={allowLetters ? "text" : "numeric"}
                        maxLength={1}
                        value={value[i] ?? ""}
                        disabled={!enabled}
                        onFocus={(e) => e.target.select()}
                        onChange={(e) => handleChange(i, e)}
                    />
                ))}
            </div>
        </div>
    );
}