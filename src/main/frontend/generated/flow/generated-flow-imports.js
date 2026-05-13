import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/app-layout/src/vaadin-drawer-toggle.js';
import '@vaadin/button/src/vaadin-button.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/icon/src/vaadin-icon.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';
import 'react-router';
import 'react';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '6c4d5897c31695b9124936b5c01a90b30a6217712e76c18eee614038beac3a6d') {
    pending.push(import('./chunks/chunk-b9438e86433ab993b3a972b640477409f1a47c5b197ba342f82b0bf833c0336c.js'));
  }
  if (key === '632658ae77be0ac7976f1b8bc72c03708cb03aace2ddd3557536f573971ca1c3') {
    pending.push(import('./chunks/chunk-5f6dd462696ae8ed38d1d43d163a4d2741a07600d0db894df982ad0d9629d080.js'));
  }
  if (key === '530233020ff79de88894a4902dec70689947ced858acb8b28086d3de0bc1f3d2') {
    pending.push(import('./chunks/chunk-fffbd2d71e45b110b1a7c1b1def8238814c62f65d11d1b1ba37e4f4ae28b5c94.js'));
  }
  if (key === '2c2ed30dba940656bd427e0350c0338b6fbcf22a9bd139a3d043ca2bb367e16e') {
    pending.push(import('./chunks/chunk-b9438e86433ab993b3a972b640477409f1a47c5b197ba342f82b0bf833c0336c.js'));
  }
  if (key === '60044551599dd8fad8846283bddb79b5e28a81c0ff53a69639ecf5e3b79df1b6') {
    pending.push(import('./chunks/chunk-8d0b6753061d2fb106fafe45413718b530698b187be91c454c924255097ad0e2.js'));
  }
  if (key === '5e0c3bafa86112e64b4bbdea126bfc4448f7e35f4dbceafe2c9874a80e9da3a9') {
    pending.push(import('./chunks/chunk-7a37db96cd34ca142a86047542497ad43308f1a98189f32cb602f7f5b1d820b9.js'));
  }
  if (key === 'c096e72d22e083df5e3ee45ada0f1b462a967c1059557716464928a17c7ade63') {
    pending.push(import('./chunks/chunk-1bb5799b5b8fdf403502cc985a3c9c62ff4042a7c574be3ae30e1e718f67935c.js'));
  }
  if (key === '78c38b9b65a3d400e7de58eed1f20abb26faeef34194e598957e98e19b238eb9') {
    pending.push(import('./chunks/chunk-63cd51eb20ababadd8df5f7d88f21962a1004eb72600c067bb4c5148baa9caa9.js'));
  }
  if (key === '770e1be2959aa186811ec862a952bcea03207572876991a4340361d254241927') {
    pending.push(import('./chunks/chunk-6ca78f5553e893fc12b96c7047e3ab968f3062b0aeb3c099ffaa6c6facf96929.js'));
  }
  if (key === 'fac87a9fb399634c7ce96c7d6e53584affb63e33c029569e4d789aec45d52916') {
    pending.push(import('./chunks/chunk-0fbceddd5e0f3ec8fb19a577d4183b51993b2fda0c5779e967f28a6873482559.js'));
  }
  if (key === '5ec55be5a6cc24e9461477bebd0f015a8a6c6717a5fef56c1e97709cff5f0e95') {
    pending.push(import('./chunks/chunk-8d0b6753061d2fb106fafe45413718b530698b187be91c454c924255097ad0e2.js'));
  }
  if (key === 'f1d6012e9e9fb9f9fe8884f9f9eec9ee18825ba83e69d5c50408dbcb563d96a1') {
    pending.push(import('./chunks/chunk-8cd171fa72c0015210257c27116af8dc8b9c186398323f3c679f4b66a06886b6.js'));
  }
  if (key === '0b7425f421960afd943805a0d49939c1392c3fc06591af2166c9e5100c7227ec') {
    pending.push(import('./chunks/chunk-8d0b6753061d2fb106fafe45413718b530698b187be91c454c924255097ad0e2.js'));
  }
  if (key === '14a182539d6a775331c77882a42c0e217e807ddc00d427af5d1cea3b80206f33') {
    pending.push(import('./chunks/chunk-9c9f54ed41d823bcd6b5299ac5de222867b617cb1f0e553a4f2a5d334f70b58c.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}