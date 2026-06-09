import React, { useState } from "react";
import "./PinInput.css";

// ---------------------------------------------------------------------------
// This is a standalone React component — no Flowable wiring yet.
// Your job is to integrate it with Flowable Forms using useCustomComponent.
// ---------------------------------------------------------------------------

interface PinInputProps {
    length?: number;
    masked?: boolean;
    allowLetters?: boolean;
}

export default function PinInput({ length = 4, masked = true, allowLetters = false }: PinInputProps) {
    const [value, setValue] = useState("");


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

    return (
        <div>
            <div className="pin-input">
                {[...Array(length)].map((_, i) => (
                    <input
                        key={i}
                        className="pin-input__box"
                        type={masked ? "password" : "text"}
                        inputMode={allowLetters ? "text" : "numeric"}
                        maxLength={1}
                        value={value[i] ?? ""}
                        onFocus={(e) => e.target.select()}
                        onChange={(e: any) => handleChange(i, e)}
                    />
                ))}
            </div>
        </div>
    );
}