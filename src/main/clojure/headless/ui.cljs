(ns headless.ui
  (:require
    ["@headlessui/react" :as ui]
    [reagent.core :as r]))


(def combobox (r/adapt-react-class ui/Combobox))
(def combobox-button (r/adapt-react-class ui/Combobox.Button))
(def combobox-input (r/adapt-react-class ui/Combobox.Input))
(def combobox-label (r/adapt-react-class ui/Combobox.Label))
(def combobox-options (r/adapt-react-class ui/Combobox.Options))
(def dialog (r/adapt-react-class ui/Dialog))
(def dialog-backdrop (r/adapt-react-class ui/Dialog.Backdrop))
(def dialog-description (r/adapt-react-class ui/Dialog.Description))
(def dialog-overlay (r/adapt-react-class ui/Dialog.Overlay))
(def dialog-panel (r/adapt-react-class ui/Dialog.Panel))
(def dialog-title (r/adapt-react-class ui/Dialog.Title))
(def disclosure (r/adapt-react-class ui/Disclosure))
(def disclosure-button (r/adapt-react-class ui/Disclosure.Button))
(def disclosure-panel (r/adapt-react-class ui/Disclosure.Panel))
(def listbox (r/adapt-react-class ui/Listbox))
(def listbox-backdrop (r/adapt-react-class ui/Listbox.Button))
(def listbox-label (r/adapt-react-class ui/Listbox.Label))
(def listbox-option (r/adapt-react-class ui/Listbox.Option))
(def listbox-options (r/adapt-react-class ui/Listbox.Options))
(def menu (r/adapt-react-class ui/Menu))
(def menu-button (r/adapt-react-class ui/Menu.Button))
(def menu-item (r/adapt-react-class ui/Menu.Item))
(def menu-items (r/adapt-react-class ui/Menu.Items))
(def popover (r/adapt-react-class ui/Popover))
(def popover-button (r/adapt-react-class ui/Popover.Button))
(def popover-group (r/adapt-react-class ui/Popover.Group))
(def popover-overlay (r/adapt-react-class ui/Popover.Overlay))
(def popover-panel (r/adapt-react-class ui/Popover.Panel))
(def portal (r/adapt-react-class ui/Portal))
(def portal-group (r/adapt-react-class ui/Portal.Group))
(def radio-group (r/adapt-react-class ui/RadioGroup))
(def radio-group-description (r/adapt-react-class ui/RadioGroup.Description))
(def radio-group-label (r/adapt-react-class ui/RadioGroup.Label))
(def radio-group-option (r/adapt-react-class ui/RadioGroup.Option))
(def switch (r/adapt-react-class ui/Switch))
(def switch-description (r/adapt-react-class ui/Switch.Description))
(def switch-group (r/adapt-react-class ui/Switch.Group))
(def switch-label (r/adapt-react-class ui/Switch.Label))
(def tab (r/adapt-react-class ui/Tab))
(def tab-group (r/adapt-react-class ui/Tab.Group))
(def tab-list (r/adapt-react-class ui/Tab.List))
(def tab-panel (r/adapt-react-class ui/Tab.Panel))
(def tab-panels (r/adapt-react-class ui/Tab.Panels))
(def transition (r/adapt-react-class ui/Transition))
(def transition-child (r/adapt-react-class ui/Transition.Child))
(def transition-root (r/adapt-react-class ui/Transition.Root))