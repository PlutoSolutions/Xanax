package cat.yoink.eventmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yoink
 * @since November 23rd 2020
 * @version 1
 */
public final class EventManager
{
    private final Map<Object, List<Method>> subscribers = new HashMap<>();

    public <E> E dispatch(final E event)
    {
        for (int i = 0; i < subscribers.size(); i++)
        {
            Object instance = subscribers.keySet().toArray()[i];
            subscribers.get(instance).stream().filter(method -> method.getParameterTypes()[0].equals(event.getClass())).forEach(method -> {
                try { method.invoke(instance, event); }
                catch (final IllegalAccessException | InvocationTargetException e) { e.printStackTrace(); }
            });
        }

        return event;
    }

    public void addSubscriber(final Object subscriber)
    {
        subscribers.put(subscriber, Arrays.stream(subscriber.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Listener.class))
                .sorted(Comparator.comparing(m -> -m.getAnnotation(Listener.class).value()))
                .collect(Collectors.toList()));
    }

    public void removeSubscriber(final Object subscriber)
    {
        subscribers.remove(subscriber);
    }
}
