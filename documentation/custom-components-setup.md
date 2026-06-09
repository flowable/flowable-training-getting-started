# Custom Components – Frontend Environment Setup

This guide walks you through setting up the toolchain required to develop, preview, and upload custom Flowable form components.

---

## Prerequisites

Install the following tools before proceeding:

1. **Node.js 18+** — [nodejs.org/en/download](https://nodejs.org/en/download)

2. **Yarn (modern, 2.x+)**
   ```sh
   npm install -g corepack
   ```
   You won't need to run Yarn commands manually — the Flowable CLI handles that for you.

3. **Flowable CLI**
   ```sh
   npm install -g flowable-cli
   ```

---

## Step 1 – Get Your Artifactory NPM Token

The `@flowable` npm packages are hosted in Flowable's Artifactory instance.

1. Go to [https://flowable.jfrog.io/ui/packages](https://flowable.jfrog.io/ui/packages)
2. Log in with the credentials from your `settings.xml` or as provided by your instructor.
3. Click the **avatar** in the top right → **"Set Me Up"**
4. Under **Package Type**, select **npm**
5. Click **"Search for a repository"** and select `flowable-npm-all`
6. Make sure **"Generate Token"** is selected
7. Click **"Generate Token & Create Instructions"**
8. Copy the generated **NPM registry token** — you'll need it in the next step.

---

## Step 2 – Log In with the Flowable CLI

Open a terminal in the folder where you cloned the training project (or wherever you want your component library to live).

```sh
flowable login
```

When prompted:

| Prompt | Value |
|---|---|
| User | Same username as Artifactory (from `settings.xml` or your instructor) |
| Token | The NPM token generated in Step 1 |

Press **Enter**. You are now authenticated against Artifactory and have access to `@flowable` packages.

> **Tip:** If you run into scope resolution issues later, check the .npmrc file in your user folder. 
> It should look similar to this:
> ```md
> //flowable.jfrog.io/artifactory/api/npm/flowable-npm/:_authToken=YOUR_TOKEN
> @flowable:registry=https://flowable.jfrog.io/artifactory/api/npm/flowable-npm/
> ```

---

## Step 3 – Log the CLI Into Your Local Flowable Design

The CLI also needs to communicate with your running Flowable Design instance to push component definitions and generate types.

Keep the console open from the previous step and follow the prompts:

| Prompt | Value |
|---|---|
| Hosting location | `http://localhost:8091` |
| Environment name | `local` |

The CLI will pause and wait for a **Design access token**.

---

## Step 4 – Generate a Flowable Design Access Token

1. Make sure Flowable Design is running on port `8091`
2. Log in at [http://localhost:8091](http://localhost:8091) with `admin` / `test`
3. Click the **avatar** in the top right → **"Tokens"**  
   (or navigate directly to [http://localhost:8091/#/token-mgmt](http://localhost:8091/#/token-mgmt))
4. Click **"New Access Token"**
    - Name: `Local Development`
    - Valid for: `1 year`
5. Click **Create** and **copy the token**
6. Paste it back into your console and press **Enter**

You are now fully authenticated. Your CLI configuration is saved to:
- macOS/Linux: `~/.flowable/cli-config.yml`
- Windows: `C:\Users\<your_user>\.flowable\cli-config.yml`

You can add more environments and stages later and switch between them.
---

## Next Steps

Continue with [Custom Components – Creating a Component](./custom-components-development.md) to initialise your component library and build the Credit Card widget.
