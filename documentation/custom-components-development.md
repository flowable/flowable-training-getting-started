# Custom Components – Creating a Component

This guide walks through creating a custom Flowable form component library, adding a component, and testing it in the playground.

> **Before you start:** Complete [Custom Components – Frontend Environment Setup](./custom-components-setup.md) first.

---

## Step 1 – Initialise a Component Library

Run the following in the folder where you want the library to live (e.g. the root of the training project):

```sh
flowable components init
```

When prompted for a name, enter:

```
training-components
```

This creates a new `training-components/` folder with the project scaffold.

Once done, verify your library appears in Flowable Design:
[http://localhost:8091/#/custom-components/training-components](http://localhost:8091/#/custom-components/training-components)

---

## Step 2 – Install Dependencies

```sh
cd training-components
npm install
```

---

## Step 3 – Create the Credit Card Component

```sh
flowable components create
```

When prompted for a name, enter:

```
CreditCardSOLUTION
```

This generates `training-components/components/CreditCardSOLUTION/CreditCardSOLUTION.tsx` and registers the component in your library.

---

## Step 4 – Configure Component Properties in Flowable Design

Before writing any code, define what the **modeller** will see when they drop the component onto a form.

Open the component configuration:
[http://localhost:8091/#/custom-components/training-components/CreditCard](http://localhost:8091/#/custom-components/training-components/CreditCard)

Here you configure the properties available in the palette entry of your component.
---

## Step 5 – Start the Development Server

```sh
flowable components watch
```

This starts a live-reload dev server. Any changes you make to `CreditCardSOLUTION.tsx` are reflected immediately inside Flowable Design without a rebuild.

---

## Step 6 – Implement the Component

Open `training-components/components/CreditCardSOLUTION/CreditCardSOLUTION.tsx` in your IDE and implement the component logic.

Test it in the **Form Playground** (with dev mode enabled so your local dev server is used):
[http://localhost:8091/#/workspace/playground/apps/playground-app/editor/form/playground-admin?devMode=true](http://localhost:8091/#/workspace/playground/apps/playground-app/editor/form/playground-admin?devMode=true)

> **Tip:** The playground lets you drop your component onto a form and see its output in real time without deploying a process.

---

## Step 7 – Build and Upload

When your component is ready:

```sh
flowable components build
```

To upload as a **draft** (visible only to you for review):

```sh
flowable components upload --draft
```

To publish the component to all users:

```sh
flowable components upload --patch
```

> **Version note:** `--patch` increments the patch version (e.g. `1.0.0` → `1.0.1`). Use `--minor` or `--major` for larger changes.

---

## Quick Reference

| Command | What it does |
|---|---|
| `flowable components init` | Scaffold a new component library |
| `flowable components create` | Add a new component to the library |
| `flowable components watch` | Start live-reload dev server |
| `flowable components build` | Build the library for upload |
| `flowable components upload --draft` | Upload as draft (author only) |
| `flowable components upload --patch` | Publish with patch version bump |
| `flowable components types` | Regenerate TypeScript types from Design config |
