import { create, StateCreator } from 'zustand';
import { devtools } from 'zustand/middleware';

type OverlayState = {
  isFlowableUnavailable: boolean;
  setFlowableUnavailable: (value: boolean) => void;
};

const overlayStore: StateCreator<OverlayState> = set => ({
  isFlowableUnavailable: false,
  setFlowableUnavailable: (value: boolean) => set({ isFlowableUnavailable: value }),
});

export const useOverlayStore = create<OverlayState>()(devtools(overlayStore));
