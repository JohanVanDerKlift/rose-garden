package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.repository.WebOrderDetailRepository;
import org.springframework.stereotype.Service;

@Service
public class WebOrderDetailService {

    private final WebOrderDetailRepository webOrderDetailRepository;

    public WebOrderDetailService(WebOrderDetailRepository webOrderDetailRepository) {
        this.webOrderDetailRepository = webOrderDetailRepository;
    }
}
