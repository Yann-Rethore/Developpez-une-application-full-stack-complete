// import type { SharedPageTestArgs } from '@bgotink/playwright-coverage/fixtures';
// If you have the correct type location, update the path below:
import SharedPageTestArgs from '@bgotink/playwright-coverage';

declare module '@bgotink/playwright-coverage' {
  interface TestArgs extends SharedPageTestArgs {}
}