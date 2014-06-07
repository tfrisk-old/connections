(ns connections.core)

(enable-console-print!)

(println "Hello world!")

(defn validate-form []
  (let [text (.getElementById js/document "search-text")]
    (if (> (count (.-value text)) 0)
      true
      (do (js/alert "Empty search")
        false))))

;attach validate-form to submit event
(defn init []
  ;verify that js/document exists and it has getElementById
  (if (and js/document (.-getElementById js/document))
    (set! (.-onsubmit (.getElementById js/document "search-form")) validate-form)))

;init page un unobtrusive way
(set! (.-onload js/window) init)

